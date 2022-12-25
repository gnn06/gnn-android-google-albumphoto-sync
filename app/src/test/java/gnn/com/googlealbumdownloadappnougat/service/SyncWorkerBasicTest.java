package gnn.com.googlealbumdownloadappnougat.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerDelayedAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerWorker;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.sync.Synchronizer;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SyncWorker.class, SynchronizerDelayedAndroid.class})
// Mock simplement avec mockito un Worker
// N'utilise ni WorkManagerTestInitHelper ni TestWorkerBuilderl
// ca a l'avantage de ne pas avoir à s'éxécuter dans le virtual device
// ca permet surtout d'utiliser powermock pour mocker les private et les news
//@Ignore
public class SyncWorkerBasicTest {

    @Mock
    Context context;

    @Mock
    WorkerParameters parameters;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    private SyncWorker UT_myWorker;
    private File destinationFolder;
    private Data data;
    private SynchronizerAndroid synchronizerMock;
    private SynchronizerWorker observer;
    private SynchronizerDelayedAndroid synchronizerDelayedMock;
    @Mock
    private PersistOauthError persistOauth;

    @Before
    public void setUp() throws Exception {
        Logger.configure();
        destinationFolder = tmpFolder.newFolder();
        when(context.getFilesDir()).thenReturn(destinationFolder);
        ApplicationContext.getInstance(context);
        UT_myWorker = PowerMockito.spy(new SyncWorker(context, parameters));
        persistOauth = spy(new PersistOauthError(ApplicationContext.getInstance(context).getProcessFolder()));
        UT_myWorker = PowerMockito.spy(new SyncWorker(context, parameters, persistOauth));
        data = new Data.Builder()
                .putString("cacheAbsolutePath", tmpFolder.newFile().getAbsolutePath())
                .putLong("cacheMaxAge", -1)
                .putString("processAbsolutePath", tmpFolder.newFolder().getAbsolutePath())

                .putString("album", "album")
                .putString("folderPath", destinationFolder.getAbsolutePath())
                .putString("rename", null)
                .putInt("quantity", -1)

                .build();
        when(UT_myWorker.getInputData()).thenReturn(data);
        // use powerMockito to mock private getFilename method
        // and use doReturn to avoid null pointer exception caused by when-thenReturn
        PowerMockito.doReturn(destinationFolder).when(UT_myWorker, "getDestinationFolder", anyString());
        synchronizerMock = mock(SynchronizerAndroid.class);
        doCallRealMethod().when(synchronizerMock).setObserver(observer);
        synchronizerDelayedMock = spy(new SynchronizerDelayedAndroid(12, null, null, -1, null, synchronizerMock));
        observer = mock(SynchronizerWorker.class);
    }

    @After
    public void tearDown() {
        ApplicationContext.getInstance(null).reset();
    }

    @Test
    public void test_success() throws Exception {
        PowerMockito.whenNew(SynchronizerDelayedAndroid.class).withAnyArguments().thenReturn(synchronizerDelayedMock);

//        Context mockContext = mock(Context.class);
//        when(mockContext.getFilesDir()).thenReturn(tmpFolder.newFolder());
//        ApplicationContext.getInstance(mockContext);

        // when
        ListenableWorker.Result result = UT_myWorker.doWork();

        assertThat(result, is(ListenableWorker.Result.success()));
    }

    @Test
    public void delayed() throws Exception {
        PowerMockito.whenNew(SynchronizerDelayedAndroid.class).withAnyArguments().thenReturn(synchronizerDelayedMock);

//        Context mockContext = mock(Context.class);
//        when(mockContext.getFilesDir()).thenReturn(tmpFolder.newFolder());
//        ApplicationContext.getInstance(mockContext);

        // when
        ListenableWorker.Result result = UT_myWorker.doWork();

        assertThat(result, is(ListenableWorker.Result.success()));
    }

    @Test
    public void test_exception() throws Exception {
        doThrow(new RemoteException(null)).when(synchronizerMock).syncRandom("album", destinationFolder, null, -1);
        PowerMockito.whenNew(SynchronizerDelayedAndroid.class).withAnyArguments().thenReturn(synchronizerDelayedMock);

        // when
        ListenableWorker.Result result = UT_myWorker.doWork();

        assertThat(result, is(ListenableWorker.Result.failure()));
        verify(UT_myWorker).syncOauthException();
        boolean errorFileExists = new File(destinationFolder.getAbsolutePath(), "oauth_error").exists();
        assertThat(errorFileExists, is(true));
    }

    @Test
    public void reset_oauth_error_exists() throws Exception {
        FileUtils.write(new File(destinationFolder.getAbsolutePath(), "oauth_error"), "error");
        PowerMockito.whenNew(SynchronizerDelayedAndroid.class).withAnyArguments().thenReturn(synchronizerDelayedMock);
        InOrder inOrder = inOrder(persistOauth, synchronizerDelayedMock);

        // when
        ListenableWorker.Result result = UT_myWorker.doWork();

        // then
        assertThat(result, is(ListenableWorker.Result.success()));
        inOrder.verify(persistOauth).reset();
        inOrder.verify(synchronizerDelayedMock).syncRandom(anyString(), any(), eq(null), anyInt());
    }

}