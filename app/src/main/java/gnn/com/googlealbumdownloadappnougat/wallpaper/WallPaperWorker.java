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
        String destinationPath = getInputData().getString("folderPath");;
        File destinationFolder = getDestinationFolder(destinationPath);
        PhotoWallPaper photoWallPaper = new PhotoWallPaper((Activity)getApplicationContext(), destinationFolder);
        photoWallPaper.setWallpaper();
        return Result.success();
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
