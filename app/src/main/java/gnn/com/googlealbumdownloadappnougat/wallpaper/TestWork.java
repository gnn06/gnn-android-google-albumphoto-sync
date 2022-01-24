package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TestWork extends Worker {

    public TestWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.d("GOI","work create");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("GOI", "doWork");
        return Result.success();
    }
}