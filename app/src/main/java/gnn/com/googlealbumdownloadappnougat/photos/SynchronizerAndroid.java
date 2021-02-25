package gnn.com.googlealbumdownloadappnougat.photos;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerAndroid extends Synchronizer {

    private final MainActivity activity;

    public SynchronizerAndroid(MainActivity activity, File cacheFile, long cacheMaxAge, File processFolder) {
        super(cacheFile, cacheMaxAge, processFolder);
        this.activity = activity;
    }

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
        this.albumSize++;
        this.syncTask.publicPublish();
    }


}
