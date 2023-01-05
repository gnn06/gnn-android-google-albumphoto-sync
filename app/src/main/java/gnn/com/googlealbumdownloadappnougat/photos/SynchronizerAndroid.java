package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import java.io.File;

import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerAndroid extends Synchronizer {

    // transmited to PhotoSRemoteService
    private final Context activity;

    public SynchronizerAndroid(Context activity, File cacheFile, long cacheMaxAgeHour, File processFolder) {
        super(cacheFile, cacheMaxAgeHour, processFolder, null);
        this.activity = activity;
    }

    @Override
    protected PhotosRemoteService getRemoteServiceImpl() {
        return new PhotosRemoteServiceAndroid(activity, cacheFile, cacheMaxAgeHour);
    }
}
