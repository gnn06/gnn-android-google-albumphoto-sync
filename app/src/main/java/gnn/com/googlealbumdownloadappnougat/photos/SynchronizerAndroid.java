package gnn.com.googlealbumdownloadappnougat.photos;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.remote.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerAndroid extends Synchronizer {

    private SyncTask syncTask;
    private final MainActivity activity;

    public SynchronizerAndroid(MainActivity activity) {
        this.activity = activity;
    }

    public void setSyncTask(SyncTask syncTask) {
        this.syncTask = syncTask;
    }

    protected PhotosRemoteService getRemoteService() {
        return new PhotosRemoteServiceAndroid(activity);
    }

    protected void incCurrentDownload() {
        super.incCurrentDownload();
        this.syncTask.publicPublish();
    }

    public void incCurrentDelete() {
        super.incCurrentDelete();
        this.syncTask.publicPublish();
    }

    @Override
    public void incAlbumSize() {
        this.albumSize++;
        this.syncTask.publicPublish();
    }


}
