package gnn.com.googlealbumdownloadappnougat.wallpaper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.core.app.ApplicationProvider;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import gnn.com.googlealbumdownloadappnougat.AppScheduler;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;

@RunWith(RobolectricTestRunner.class)
public class AppSchedulerTest {

    private Context context;
    private Configuration config;
    private PresenterHome presenter;
    private TestDriver testDriver;
    private PeriodicWorkRequest request;
    private WorkManager workManager;
    private LifecycleOwner testLifeCycle;

    @Rule public TestRule rule = new InstantTaskExecutorRule();

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
    public void register_with_existing_work() {
        // Given
        AppScheduler scheduler = new WallpaperScheduler(context);
        workManager.enqueueUniquePeriodicWork(scheduler.getWorkName(), ExistingPeriodicWorkPolicy.REPLACE, request);
        scheduler.registerWorkerObserver(presenter, testLifeCycle);

        // Then, observer is executed immediately
        verify(presenter).refreshLastTime();
    }

    @Test
    public void register_with_no_work() {
        // Given
        AppScheduler scheduler = new WallpaperScheduler(context);
        scheduler.registerWorkerObserver(presenter, testLifeCycle);

        // Then
        verify(presenter, never()).refreshLastTime();
    }

    @Test
    public void register_then_enqueue() {
        // Given
        AppScheduler scheduler = new WallpaperScheduler(context);
        scheduler.registerWorkerObserver(presenter, testLifeCycle);
        workManager.enqueueUniquePeriodicWork(scheduler.getWorkName(), ExistingPeriodicWorkPolicy.REPLACE, request);

        // Then
        verify(presenter).refreshLastTime();
    }

    @Test
    public void register_then_execute() throws ExecutionException, InterruptedException {
        // Given
        AppScheduler scheduler = new WallpaperScheduler(context);
        scheduler.registerWorkerObserver(presenter, testLifeCycle);
        workManager.enqueueUniquePeriodicWork(scheduler.getWorkName(), ExistingPeriodicWorkPolicy.REPLACE, request);
        testDriver.setPeriodDelayMet(request.getId());

        // Then
        verify(presenter).refreshLastTime();
    }

    @Test
    public void workerChanged_wallpaper_enqueued() {
        // Given
        AppScheduler scheduler = new WallpaperScheduler(context);
        List<WorkInfo> workInfos = new ArrayList<>();
        WorkInfo info = new WorkInfo(mock(UUID.class), WorkInfo.State.ENQUEUED, mock(Data.class), new ArrayList<>(), mock(Data.class), 0);
        workInfos.add(info);

        // When
        scheduler.onWorkerChanged(workInfos, presenter);

        // Then
        verify(presenter).refreshLastTime();
        verify(presenter, never()).refreshLastSyncResult();
        verify(presenter, never()).setSyncResult(any(), any());;
    }

    @Test
    public void workerChanged_wallpaper_running() {
        // Given
        AppScheduler scheduler = new WallpaperScheduler(context);
        List<WorkInfo> workInfos = new ArrayList<>();
        WorkInfo info = new WorkInfo(mock(UUID.class), WorkInfo.State.RUNNING, mock(Data.class), new ArrayList<>(), mock(Data.class), 0);
        workInfos.add(info);

        // When
        scheduler.onWorkerChanged(workInfos, presenter);

        // Then
        verify(presenter, never()).refreshLastTime();
        verify(presenter, never()).refreshLastSyncResult();
        verify(presenter, never()).setSyncResult(any(), any());;
    }

    @Test
    public void workerChanged_sync_enqueued() {
        // Given
        AppScheduler scheduler = new SyncScheduler(context);
        List<WorkInfo> workInfos = new ArrayList<>();
        WorkInfo info = new WorkInfo(mock(UUID.class), WorkInfo.State.ENQUEUED, mock(Data.class), new ArrayList<>(), mock(Data.class), 0);
        workInfos.add(info);

        // When
        scheduler.onWorkerChanged(workInfos, presenter);

        // Then
        verify(presenter).refreshLastTime();
        verify(presenter).refreshLastSyncResult();
        verify(presenter, never()).setSyncResult(any(), any());
    }

    @Test
    public void workerChanged_sync_running_starting() {
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
        // Check running is called with same step as given
        verify(presenter, never()).refreshLastTime();
        verify(presenter, never()).refreshLastSyncResult();
        verify(presenter).setSyncResult(any(), eq(SyncStep.STARTING));
    }
}