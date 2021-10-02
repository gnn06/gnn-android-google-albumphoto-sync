package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import java.io.File;

import gnn.com.photos.sync.SynchronizerDelayed;

public class SynchronizerDelayedAndroid extends SynchronizerDelayed {

    public SynchronizerDelayedAndroid(long delay, Context activity, File cacheFile, long cacheMaxAge, File processFolder) {
        super(delay);
        this.synchronizer = new SynchronizerAndroid(activity, cacheFile, cacheMaxAge, processFolder);
    }
}