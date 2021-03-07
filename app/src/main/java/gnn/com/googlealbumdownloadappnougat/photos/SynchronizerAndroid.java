package gnn.com.googlealbumdownloadappnougat.photos;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerAndroid extends Synchronizer {

    // transmited to PhotoSRemoteService
    private final MainActivity activity;

    public SynchronizerAndroid(MainActivity activity, File cacheFile, long cacheMaxAge, File processFolder) {
        super(cacheFile, cacheMaxAge, processFolder);
        this.activity = activity;
    }

    // to forward progression
    private SyncTask syncTask;

    public void setSyncTask(SyncTask syncTask) {
        this.syncTask = syncTask;
    }

    @Override
    protected PhotosRemoteService getRemoteServiceImpl() {
        return new PhotosRemoteServiceAndroid(activity, cacheFile, cacheMaxAge);
    }

    public void incCurrentDownload() {
        super.incCurrentDownload();
        this.syncTask.publicPublish();
    }

    public void incCurrentDelete() {
        super.incCurrentDelete();
        this.syncTask.publicPublish();
    }

    @Override
    public void incAlbumSize() {
        super.incAlbumSize();
        this.syncTask.publicPublish();
    }


}
