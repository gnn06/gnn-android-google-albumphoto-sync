package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.sync.Synchronizer;

public class MyWorker extends Worker {

    private final static String TAG = "MyWorker";
    private final static String LOG_FILENAME = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
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
            log(Level.INFO, "success");
            Log.i(TAG, "success");
            return Result.success();
        } catch (IOException | RemoteException e) {
            log(Level.SEVERE, e.toString());
            Log.e(TAG, e.toString());
            return Result.failure();
        }
    }

    private void log(Level level, String message) {
        Logger logger = getLogger();
        if (logger != null) {
            logger.log(level, message);
        }
    }

    private Logger getLogger() {
        Logger logger = Logger.getLogger(MyWorker.class.getName());
        if (logger.getHandlers().length == 0) {
            try {
                FileHandler fileHandler = new FileHandler(getFilename(), true);
                fileHandler.setFormatter(new SimpleFormatter());
                // keep parent handler
                logger.addHandler(fileHandler);
                Log.i(TAG, "logger initialized");
            } catch (Exception ex) {
                Log.e(TAG, "can not get logger");
                logger = null;
            }
        }
        return logger;
    }

    private String getFilename() {
        return getApplicationContext().getFilesDir().getAbsolutePath() + LOG_FILENAME;
    }

}
