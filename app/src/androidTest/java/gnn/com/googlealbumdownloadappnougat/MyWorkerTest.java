package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestWorkerBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MyWorkerTest {

    private Context context;
    private Executor executor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void doWork() {
        MyWorker worker = TestWorkerBuilder.from(context, MyWorker.class, executor)
                .build();
        ListenableWorker.Result result = worker.doWork();
    }
}