package gnn.com.googlealbumdownloadappnougat.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ScreenSizeAccessor;
import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerDelayedAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerWorker;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.FrequencyCacheDelayConverter;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallPaperWorker;
import gnn.com.photos.LibContext;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;
import gnn.com.photos.sync.SynchronizerDelayed;

public class SyncWorker extends Worker implements  ISyncOauth<Worker.Result> {

    private final static String TAG = "MyWorker";
    private final PersistOauthError persistOauthError;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        persistOauthError = new PersistOauthError(ApplicationContext.getInstance(getApplicationContext()).getProcessFolder());
    }

    // For test
    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams, PersistOauthError persistOauth) {
        super(context, workerParams);
        persistOauthError = persistOauth;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            return execOauth();
        } catch (RemoteException e) {
            return Result.failure();
        }
    }

    @Override
    public ListenableWorker.Result execOauthImpl() throws RemoteException {
        PersistPrefMain persistPrefMain = new PersistPrefMain(getApplicationContext());

        File cacheFile = new File(getInputData().getString(WallPaperWorker.PARAM_CACHE_ABSOLUTE_PATH));
        int frequencyUpdateHour = persistPrefMain.getFrequencyUpdatePhotos();
        File processFolder = new File(getInputData().getString(WallPaperWorker.PARAM_PROCESS_ABSOLUTE_PATH));

        int frequencySyncHour = persistPrefMain.getFrequencyDownload();

        int delayUpdateHour = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosHourHour(frequencyUpdateHour);
        int delaySyncMin = FrequencyCacheDelayConverter.getFrequencySyncHourMinute(frequencySyncHour);

        ScreenSizeAccessor screenSizeAccessor = new ScreenSizeAccessor(getApplicationContext());
        Context activity = getApplicationContext();
        SynchronizerDelayed synchronizer = new SynchronizerDelayedAndroid(delaySyncMin, activity, cacheFile, delayUpdateHour, processFolder, screenSizeAccessor);
        SyncProgressObserver observer = new SynchronizerWorker(this);
        synchronizer.setObserver(observer);

        String destinationFolder = persistPrefMain.getPhotoPath();
        String albumName = persistPrefMain.getAlbum();
        String rename = persistPrefMain.getRename();
        int quantity = persistPrefMain.getQuantity();

        // Doc Periodic work is never successed, always enqueued

        try {
            synchronizer.syncRandom(albumName, getDestinationFolder(destinationFolder), rename, quantity);
            Log.i(TAG, "success");
            // Doc periodic outputData is always empty
            Log.d("GNNAPP","SyncWork finished");
            // With a Periodic work, output is always null as (said in android doc) output is only available in FINISHED state
            return Result.success();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return Result.failure();
        }
    }

    @Override
    public PersistOauthError getPersistOAuth() {
        return persistOauthError;
    }

    @Override
    public Result returnFailure() {
        return Result.failure();
    }

    private File getDestinationFolder(String album) {
        return Environment.getExternalStoragePublicDirectory(album);
    }

}
