package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestWorkerBuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
public class MyWorkerTest {

    private Context context;
    private Executor executor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void doWork() throws IOException {
        File cache = temporaryFolder.newFolder();
        File process = temporaryFolder.newFolder();
        File folder = temporaryFolder.newFolder();
        MyWorker worker = TestWorkerBuilder.from(context, MyWorker.class, executor)
                .setInputData(
                        new Data.Builder()
                        .putString("cacheAbsolutePath", cache.getAbsolutePath())
                        .putLong("cacheMaxAge", -1)
                        .putString("processAbsolutePath", process.getAbsolutePath())

                        .putString("album", "album")
                        .putString("folderPath", folder.getAbsolutePath())
                        .putString("rename", null)
                        .putInt("quantity", -1)

                        .build()
                )
                .build();
        ListenableWorker.Result result = worker.doWork();
    }
}