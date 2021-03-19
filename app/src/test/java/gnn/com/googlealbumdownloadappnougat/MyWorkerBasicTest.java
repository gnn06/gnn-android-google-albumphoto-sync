package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

        ListenableWorker.Result result = myWorker.doWork();

        assertThat(result, is(ListenableWorker.Result.success()));
    }

}