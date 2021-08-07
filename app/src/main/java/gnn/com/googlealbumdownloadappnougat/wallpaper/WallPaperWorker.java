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
import gnn.com.googlealbumdownloadappnougat.util.AppLogger;
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
        Logger logger = AppLogger.getLogger(getApplicationContext());
        try {
            logger.info("Wallpaper.doWork start");
            String destinationPath = getInputData().getString("folderPath");
            File destinationFolder = getDestinationFolder(destinationPath);
            logger.info("WallpaperWorker parameters " + destinationPath);
            PhotoWallPaper photoWallPaper = new PhotoWallPaper(getApplicationContext(), destinationFolder);
            photoWallPaper.setWallpaper();
            return Result.success();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Result.failure();
        }
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
