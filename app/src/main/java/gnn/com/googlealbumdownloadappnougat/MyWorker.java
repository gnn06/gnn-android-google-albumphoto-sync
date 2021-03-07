package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WORKER","doWork");
//        SynchronizerAndroid synchronizer = new SynchronizerAndroid(this.getApplicationContext(), cacheFile, cacheMaxage, processFolder);
//        synchronizer.syncRandom(albumName, destinationFolder, rename, quantity);
        return Result.success();
    }
}
