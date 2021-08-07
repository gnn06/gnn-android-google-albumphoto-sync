package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.WorkerParameters;
import androidx.work.testing.TestWorkerBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import gnn.com.googlealbumdownloadappnougat.service.SyncWorker;

@RunWith(AndroidJUnit4.class)
public class WallPaperWorkerTest {

    private Context context;
    private ExecutorService executor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void getLogger() {
        WallPaperWorker worker = TestWorkerBuilder.from(context,
                WallPaperWorker.class,
                executor)
                .build();
        Logger logger = worker.getLogger();
        logger.info("unit test");
    }
}