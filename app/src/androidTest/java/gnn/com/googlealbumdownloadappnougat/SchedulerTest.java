package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * not an real unit test
 * just useful understand and check of WorkManagerTestInitializer works
 *
 * in each test method, queue is empty
 */

@RunWith(AndroidJUnit4.class)
public class SchedulerTest {

    private ListenableFuture<List<WorkInfo>> info;
    private WorkManager workManager;
    private TestDriver testDriver;
    private PeriodicWorkRequest request;
    private Scheduler UT_scheduler;
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        Context context = ApplicationProvider.getApplicationContext();
        Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(
                context, config);
        workManager = WorkManager.getInstance(context);
        testDriver = WorkManagerTestInitHelper.getTestDriver(context);
        Data data = new Data.Builder()
                .putString("cacheAbsolutePath", tmpFolder.newFile().getAbsolutePath())
                .putLong("cacheMaxAge", -1)
                .putString("processAbsolutePath", tmpFolder.newFolder().getAbsolutePath())

                .putString("album", "album")
                .putString("folderPath", tmpFolder.newFolder().getAbsolutePath())
                .putString("rename", null)
                .putInt("quantity", -1)

                .build();

        request = new PeriodicWorkRequest.Builder(WorkerMock.class, 1, TimeUnit.HOURS)
                .setInputData(data)
                .build();
        this.UT_scheduler = new Scheduler(context);
    }

    @Test
    @Ignore
    public void schedule() throws ExecutionException, InterruptedException {
        // given an empty queue
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(0));
        // when enqueue work
        workManager.enqueueUniquePeriodicWork(Scheduler.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request);
        // then assert one work is enqueued
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(1));
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.ENQUEUED));

        // when
        UT_scheduler.schedule();

        // then still one work
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(1));
    }

    @Test
    @Ignore
    public void cancel() throws ExecutionException, InterruptedException {
        // given an enqueued work (state == enqueued)
        workManager.enqueueUniquePeriodicWork(Scheduler.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request);
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(1));
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.ENQUEUED));

        // when cancel work
        UT_scheduler.cancel();

        // then
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(1));
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.CANCELLED));
    }

    @Test
    @Ignore
    public void cancel_empty() throws ExecutionException, InterruptedException {
        // given an empty queue
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(0));

        // when cancel work
        UT_scheduler.cancel();

        // then still empty queue
        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().size(), is(0));

    }

    @Test
//    @Ignore
    public void getState() throws ExecutionException, InterruptedException {
        // given a finished work
        workManager.enqueueUniquePeriodicWork(Scheduler.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                request);

        info = workManager.getWorkInfosForUniqueWork(Scheduler.WORK_NAME);
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.ENQUEUED));
        UUID id = request.getId();

        testDriver.setPeriodDelayMet(id);

        ListenableFuture<WorkInfo> workInfoById = workManager.getWorkInfoById(id);
        String toto = workInfoById.get().getOutputData().getString("toto");

        assertThat(info.get().get(0).getState(), is(WorkInfo.State.SUCCEEDED));
    }
}