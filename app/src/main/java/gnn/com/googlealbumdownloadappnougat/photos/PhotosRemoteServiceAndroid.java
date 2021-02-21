package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.photos.service.PhotoProvider;
import gnn.com.photos.service.PhotoProviderAndroid;
import gnn.com.photos.service.PhotosRemoteService;

public class PhotosRemoteServiceAndroid extends PhotosRemoteService {

    private final Context activity;

    public PhotosRemoteServiceAndroid(MainActivity activity, File cacheFile, long cacheMaxAge) {
        super(cacheFile, cacheMaxAge);
        this.activity = activity;
    }

    @Override
    public PhotoProvider getPhotoProvider() {
        return new PhotoProviderAndroid(activity);
    }
}
