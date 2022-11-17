package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TestWorker extends Worker {

    public TestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("doWork");
        return Result.success();
    }
};