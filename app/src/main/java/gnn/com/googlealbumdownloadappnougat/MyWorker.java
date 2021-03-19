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

    // given from Presenter
    private final SynchronizerAndroid synchronizer =  null;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // impossible d'injcter un synchronizer via le constructeur car on ne contrôle
    // pas l'appel du contructeur ; utiliser inputData

    @NonNull
    @Override
    public Result doWork() {
        Log.i("WORKER","doWork");

        File cacheFile = new File(getInputData().getString("cacheAbsolutePath"));
        long cacheMaxAge = getInputData().getLong("cacheMaxAge", -1);
        File processFolder = new File(getInputData().getString("processAbsolutePath"));

        Context activity = getApplicationContext();
        Synchronizer synchronizer = new SynchronizerAndroid(activity, cacheFile, cacheMaxAge, processFolder);

        String albumName = getInputData().getString("album");
        File destinationFolder = new File(getInputData().getString("folderPath"));
        String rename = getInputData().getString("rename");
        int quantity = getInputData().getInt("quantity", -1);
        try {
            synchronizer.syncRandom(albumName, destinationFolder, rename, quantity);
            return Result.success();
        } catch (IOException | RemoteException e) {
            return Result.failure();
        }
    }

}
