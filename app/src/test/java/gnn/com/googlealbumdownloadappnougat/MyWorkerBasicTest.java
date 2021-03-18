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
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    public void test() {
        MyWorker myWorker = new MyWorker(context, parameters);
    }

}