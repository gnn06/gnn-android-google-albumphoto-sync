package gnn.com.googlealbumdownloadappnougat.photos;

import androidx.work.Data;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.service.SyncWorker;
import gnn.com.photos.service.SyncProgressObserver;

public class SynchronizerWorker implements SyncProgressObserver {

    private final SyncWorker worker;

    public SynchronizerWorker(SyncWorker worker) {
        this.worker = worker;
    }

    @Override
    public void incCurrentDownload() {
        worker.setProgressAsync(new Data.Builder()
                .putString("GOI-STEP", SyncStep.IN_PRORGESS.name())
                .build());
    }

    @Override
    public void incCurrentDelete() {
        worker.setProgressAsync(new Data.Builder()
                .putString("GOI-STEP", SyncStep.IN_PRORGESS.name())
                .build());
    }

    @Override
    public void incAlbumSize() {
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
