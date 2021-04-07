package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.sync.Synchronizer;

public class MyWorker extends Worker {

    private final static String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WorkerResultStore store = new WorkerResultStore(getApplicationContext());

        File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
        long cacheMaxAge = getInputData().getLong("cacheMaxAge", -1);
        File processFolder = new File(getInputData().getString("processAbsolutePath"));

        Context activity = getApplicationContext();
        Synchronizer synchronizer = new SynchronizerAndroid(activity, cacheFile, cacheMaxAge, processFolder);

        String albumName = getInputData().getString("album");
        String destinationFolder = getInputData().getString("folderPath");
        String rename = getInputData().getString("rename");
        int quantity = getInputData().getInt("quantity", -1);

        // Doc Periodic work is never successed, always enqueued

        try {
            synchronizer.syncRandom(albumName, new File(destinationFolder), rename, quantity);
            store.store(WorkerResultStore.State.SUCCESS);
            Log.i(TAG, "success");
            // Doc periodic outputData is always empty
            return Result.success();
        } catch (IOException | RemoteException e) {
            try {
                store.store(WorkerResultStore.State.FAILURE);
            } catch (IOException ioException) {
                Log.e(TAG, "can not store result");
            }
            Log.e(TAG, e.toString());
            return Result.failure();
        }
    }

    private String getFilename() {
        return getApplicationContext().getFilesDir().getAbsolutePath();
    }

}
