package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.sync.Synchronizer;

public class SyncWorker extends Worker {

    private final static String TAG = "MyWorker";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        PersistPrefMain persistPrefMain = new PersistPrefMain(getApplicationContext());

        File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
        long cacheMaxAgeHour = persistPrefMain.getFrequencyUpdatePhotos();
        File processFolder = new File(getInputData().getString("processAbsolutePath"));

        Context activity = getApplicationContext();
        Synchronizer synchronizer = new SynchronizerAndroid(activity, cacheFile, cacheMaxAgeHour, processFolder);

        String albumName = getInputData().getString("album");
        String destinationFolder = getInputData().getString("folderPath");
        String rename = getInputData().getString("rename");
        int quantity = getInputData().getInt("quantity", -1);

        // Doc Periodic work is never successed, always enqueued

        try {
            synchronizer.syncRandom(albumName, getDestinationFolder(destinationFolder), rename, quantity);
            Log.i(TAG, "success");
            // Doc periodic outputData is always empty
            return Result.success();
        } catch (IOException | RemoteException e) {
            Log.e(TAG, e.toString());
            return Result.failure();
        }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
