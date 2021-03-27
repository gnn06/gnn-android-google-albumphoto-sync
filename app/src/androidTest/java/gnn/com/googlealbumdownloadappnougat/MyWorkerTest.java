package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.TestWorkerBuilder;
import androidx.work.testing.WorkManagerTestInitHelper;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * not an real unit test
 * just usefull understand and check of WorkManagerTestInitializer works
 *
 * in each test method, queue is empty
 */

@RunWith(AndroidJUnit4.class)
public class MyWorkerTest {

    public static final String MY_WORKER = "MyWorker";
    private  Context context;
    private ListenableFuture<List<WorkInfo>> info;
    private WorkManager workManager;
    private PeriodicWorkRequest request;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(
                context, config);
        workManager = WorkManager.getInstance(context);
        request = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.HOURS)
        .build();
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        // given an empty queue
        info = workManager.getWorkInfosForUniqueWork(MY_WORKER);
        assertThat(info.get().size(), is(0));
        // when enqueue work
        workManager.enqueueUniquePeriodicWork(MY_WORKER,
                ExistingPeriodicWorkPolicy.REPLACE,
                request);
        // then assert one work is enqueued
        info = workManager.getWorkInfosForUniqueWork(MY_WORKER);
        assertThat(info.get().size(), is(1));
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.ENQUEUED));
        // and when replace work
        workManager.enqueueUniquePeriodicWork(MY_WORKER,
                ExistingPeriodicWorkPolicy.REPLACE,
                request);
        // then still one work
        info = workManager.getWorkInfosForUniqueWork(MY_WORKER);
        assertThat(info.get().size(), is(1));
    }

    @Test
    public void cancel() throws ExecutionException, InterruptedException {
        // given an enqueued work (state == enqueued)
        workManager.enqueueUniquePeriodicWork(MY_WORKER,
                ExistingPeriodicWorkPolicy.REPLACE,
                request);
        info = workManager.getWorkInfosForUniqueWork(MY_WORKER);
        assertThat(info.get().size(), is(1));
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.ENQUEUED));

        // when cancel work
        workManager.cancelUniqueWork(MY_WORKER);

        // then
        info = workManager.getWorkInfosForUniqueWork(MY_WORKER);
        assertThat(info.get().size(), is(1));
        assertThat(info.get().get(0).getState(), is(WorkInfo.State.CANCELLED));

    }
}