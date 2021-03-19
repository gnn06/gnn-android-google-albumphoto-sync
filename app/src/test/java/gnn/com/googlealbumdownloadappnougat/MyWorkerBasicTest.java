package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.work.Data;
import androidx.work.WorkerParameters;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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

    @Test
    @Ignore
    public void dummy() throws IOException {
        MyWorker worker = mock(MyWorker.class);
        when(worker.doWork()).thenCallRealMethod();
//        Data data = new Data.Builder()
//                .putString("cacheAbsolutePath", tmpFolder.newFile().getAbsolutePath())
//                .putLong("cacheMaxAge", 24)
//                .putString("processAbsolutePath", tmpFolder.newFolder().getAbsolutePath())
//
//                .putString("folderPath", tmpFolder.newFolder().getAbsolutePath())
//
//                .build();
//        doReturn(data).when(worker).getInputData();
//        when(worker.getInputData()).thenReturn(data);

        worker.doWork();
    }

    @Test
    public void test() throws Exception {
        MyWorker myWorker = new MyWorker(context, parameters);

        Data data = new Data.Builder()
                .putString("cacheAbsolutePath", tmpFolder.newFile().getAbsolutePath())
                .putLong("cacheMaxAge", -1)
                .putString("processAbsolutePath", tmpFolder.newFolder().getAbsolutePath())

                .putString("folderPath", tmpFolder.newFolder().getAbsolutePath())

                .build();
        when(myWorker.getInputData()).thenReturn(data);

        SynchronizerAndroid mock = PowerMockito.mock(SynchronizerAndroid.class);

        PowerMockito.whenNew(SynchronizerAndroid.class).withAnyArguments().thenReturn(mock);
        myWorker.doWork();
    }

}