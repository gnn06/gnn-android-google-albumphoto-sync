package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import androidx.work.Data;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.service.SyncWorker;

public class SynchronizerWorker extends SynchronizerAndroid {

    private final SyncWorker worker;

    public SynchronizerWorker(Context activity, File cacheFile, long cacheMaxAgeHour, File processFolder, SyncWorker worker) {
        super(activity, cacheFile, cacheMaxAgeHour, processFolder);
        this.worker = worker;
    }

    @Override
    public void incCurrentDownload() {
        super.incCurrentDownload();
        worker.setProgressAsync(new Data.Builder()
                .putInt("GOI", getCurrentDownload())
                .putString("GOI-STEP", SyncStep.IN_PRORGESS.name())
                .build());
    }

    @Override
    public void incCurrentDelete() {
        super.incCurrentDelete();
        worker.setProgressAsync(new Data.Builder()
                .putString("GOI-STEP", SyncStep.IN_PRORGESS.name())
                .build());
    }

    @Override
    public void incAlbumSize() {
        super.incAlbumSize();
        worker.setProgressAsync(new Data.Builder()
                .putString("GOI-STEP", SyncStep.IN_PRORGESS.name())
                .build());
    }

    @Override
    public void begin() {
        worker.setProgressAsync(new Data.Builder()
                .putString("GOI-STEP", SyncStep.STARTING.name())
                .build());
    }

    @Override
    public void end() {
        worker.setProgressAsync(new Data.Builder()
                .putString("GOI-STEP", SyncStep.FINISHED.name())
                .build());
    }
}
