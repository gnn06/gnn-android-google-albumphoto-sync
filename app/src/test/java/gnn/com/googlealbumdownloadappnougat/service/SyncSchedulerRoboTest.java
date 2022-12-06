package gnn.com.googlealbumdownloadappnougat.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.core.app.ApplicationProvider;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.AppScheduler;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.googlealbumdownloadappnougat.wallpaper.TestWorker;

@RunWith(RobolectricTestRunner.class)
public class SyncSchedulerRoboTest {

    private Context context;
    private Configuration config;
    private PresenterHome presenter;
    private TestDriver testDriver;
    private PeriodicWorkRequest request;
    private WorkManager workManager;
    private LifecycleOwner testLifeCycle;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();

        config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();

        WorkManagerTestInitHelper.initializeTestWorkManager(
                context, config);

        workManager = WorkManager.getInstance(context);

        request = new PeriodicWorkRequest.Builder(TestWorker.class, 15, TimeUnit.MINUTES)
                .addTag("gnn")
                .build();


        testDriver = WorkManagerTestInitHelper.getTestDriver(context);

        presenter = mock(PresenterHome.class);

        testLifeCycle = new TestLifecycleOwner(Lifecycle.State.STARTED);

    }

    @Test
    public void workerChanged_running_starting() {
        // Given
        AppScheduler scheduler = new SyncScheduler(context);
        List<WorkInfo> workInfos = new ArrayList<>();
        Data progress = mock(Data.class);
        when(progress.getString("GOI-STEP")).thenReturn(SyncStep.STARTING.name());
        WorkInfo info = new WorkInfo(mock(UUID.class), WorkInfo.State.RUNNING, mock(Data.class), new ArrayList<>(), progress, 0);
        workInfos.add(info);

        // When
        scheduler.onWorkerChanged(workInfos, presenter);

        // Then
        verify(presenter).onWorkerRunning(SyncStep.STARTING);
    }


}