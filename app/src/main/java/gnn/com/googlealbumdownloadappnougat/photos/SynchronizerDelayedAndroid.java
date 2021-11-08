package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import java.io.File;

import gnn.com.photos.sync.SynchronizerDelayed;

public class SynchronizerDelayedAndroid extends SynchronizerDelayed {

    public SynchronizerDelayedAndroid(int delayMinute, Context activity, File cacheFile, long cacheMaxAgeHour, File processFolder) {
        super(delayMinute);
        this.synchronizer = new SynchronizerAndroid(activity, cacheFile, cacheMaxAgeHour, processFolder);
    }
}
