package gnn.com.googlealbumdownloadappnougat.wallpaper;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.work.WorkerParameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.logging.Logger;

import gnn.com.googlealbumdownloadappnougat.service.SyncWorker;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SyncWorker.class)
public class WallPaperWorkerTest {

    @Mock
    Context context;

    @Mock
    WorkerParameters parameters;

    @Test
    public void getLogger() {
        WallPaperWorker worker = new WallPaperWorker(context, parameters);
        Logger logger = worker.getLogger();
        logger.fine("unit test");
    }
}