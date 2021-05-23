package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.Data;
import androidx.work.testing.TestWorkerBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
// use TestWorkerBuilder
// problème difficile de mocker les services instancier par le worker
// préférable de simplement mocker avec mockito. cf MyWorkerBasicTest
@Ignore
public class SyncWorkerTest {

    private Context context;
    private ExecutorService executor;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void test() throws IOException {
        Data inputData = new Data.Builder()
                .putString("cacheAbsolutePath", tmpFolder.newFile().getAbsolutePath())
                .putLong("cacheMaxAge", -1)
                .putString("processAbsolutePath", tmpFolder.newFolder().getAbsolutePath())

                .putString("album", "album")
                .putString("folderPath", tmpFolder.newFolder().getAbsolutePath())
                .putString("rename", null)
                .putInt("quantity", -1)

                .build();
        SyncWorker worker = TestWorkerBuilder.from(context,
                SyncWorker.class,
                executor)
                .setInputData(inputData)
                .build();
        worker.doWork();
    }

}
