package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.sync.Synchronizer;

public class WallPaperWorker extends Worker {

    private final static String TAG = "WalllPaperWorker";

    public WallPaperWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Logger logger = getLogger();
        String destinationPath = getInputData().getString("folderPath");;
        File destinationFolder = getDestinationFolder(destinationPath);
        PhotoWallPaper photoWallPaper = new PhotoWallPaper((Activity)getApplicationContext(), destinationFolder);
        photoWallPaper.setWallpaper();
        return Result.success();
    }

    Logger getLogger() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Handler[] handlers = logger.getHandlers();
        if (handlers.length == 0) {
            try {
                Handler handler = new FileHandler("/tmp/dev/toto.log");
                Formatter formatter = new SimpleFormatter();
                handler.setFormatter(formatter);
                logger.addHandler(handler);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return logger;
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
