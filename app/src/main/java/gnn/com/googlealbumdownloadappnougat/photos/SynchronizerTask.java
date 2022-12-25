package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;

public class SynchronizerTask extends SynchronizerAndroid {

    // to forward progression
    private SyncTask syncTask;

    public SynchronizerTask(Context activity, File cacheFile, long cacheMaxAgeHour, File processFolder) {
        super(activity, cacheFile, cacheMaxAgeHour, processFolder);
    }

    public void setSyncTask(SyncTask syncTask) {
        this.syncTask = syncTask;
    }

    public void incCurrentDownload() {
        super.incCurrentDownload();
        if (this.syncTask != null) this.syncTask.publicPublish();
    }

    public void incCurrentDelete() {
        super.incCurrentDelete();
        if (this.syncTask != null) this.syncTask.publicPublish();
    }

    @Override
    public void incAlbumSize() {
        super.incAlbumSize();
        // when called from service, syncTask is null
        if (this.syncTask != null) this.syncTask.publicPublish();
    }
}
