package gnn.com.googlealbumdownloadappnougat.wallpaper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import android.content.Context;
import android.graphics.Point;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.Data;
import androidx.work.WorkerParameters;
import androidx.work.testing.TestWorkerBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.IOException;
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

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void test_doWork() throws IOException {
        Data data = new Data.Builder()
                .putString("folderPath", tmpFolder.newFolder().getAbsolutePath())
                .build();
        WallPaperWorker worker = TestWorkerBuilder.from(context,
                WallPaperWorker.class,
                executor)
                .setInputData(data)
                .build();
        worker.doWork();
    }

    @Test
    public void test_getScreenSize() throws IOException {
        PhotoWallPaper photoWallPaper = new PhotoWallPaper(context, tmpFolder.newFolder());
        Point size = photoWallPaper.getScreenSize();
        assertThat("width", size.x, equalTo(1080));
    }

//    @Test
//    public void getLogger() {
//        WallPaperWorker worker = TestWorkerBuilder.from(context,
//                WallPaperWorker.class,
//                executor)
//                .build();
//        Logger logger = worker.getLogger();
//        logger.info("unit test");
//    }
}