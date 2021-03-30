package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerMock extends Worker {
    public WorkerMock(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("mock");
        Data outputData = new Data.Builder()
                .putString("toto", "This is output message")
                .build();
        return Result.success(outputData);
    }
}
