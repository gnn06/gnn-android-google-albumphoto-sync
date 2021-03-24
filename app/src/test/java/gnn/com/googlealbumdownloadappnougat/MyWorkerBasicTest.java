package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.photos.service.RemoteException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MyWorker.class)
public class MyWorkerBasicTest {

    @Mock
    Context context;

    @Mock
    WorkerParameters parameters;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    private MyWorker UT_myWorker;
    private File destinationFolder;
    private Data data;

    @Before
    public void setUp() throws Exception {
        UT_myWorker = PowerMockito.spy(new MyWorker(context, parameters));
        destinationFolder = tmpFolder.newFolder();
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
        PowerMockito.doReturn(tmpFolder.newFile().getAbsolutePath()).when(UT_myWorker, "getFilename");
    }

    @Test
    public void test_success() throws Exception {
        SynchronizerAndroid mock = PowerMockito.mock(SynchronizerAndroid.class);
        PowerMockito.whenNew(SynchronizerAndroid.class).withAnyArguments().thenReturn(mock);

        // when
        ListenableWorker.Result result = UT_myWorker.doWork();

        assertThat(result, is(ListenableWorker.Result.success()));
    }

    @Test
    public void test_exception() throws Exception {
        SynchronizerAndroid mock = PowerMockito.mock(SynchronizerAndroid.class);
        doThrow(new RemoteException(null)).when(mock).syncRandom("album", destinationFolder, null, -1);
        PowerMockito.whenNew(SynchronizerAndroid.class).withAnyArguments().thenReturn(mock);

        // when
        ListenableWorker.Result result = UT_myWorker.doWork();

        assertThat(result, is(ListenableWorker.Result.failure()));
    }

}