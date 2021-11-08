package gnn.com.googlealbumdownloadappnougat.photos;

import android.content.Context;

import java.io.File;

import gnn.com.photos.service.PhotoProviderAndroid;
import gnn.com.photos.service.PhotosRemoteService;

public class PhotosRemoteServiceAndroid extends PhotosRemoteService {

    private final Context activity;
    private PhotoProviderAndroid _provider;

    public PhotosRemoteServiceAndroid(Context activity, File cacheFile, long cacheMaxAgeHour) {
        super(cacheFile, cacheMaxAgeHour);
        this.activity = activity;
    }

    @Override
    public PhotoProviderAndroid getPhotoProvider() {
        if (_provider == null) {
            // can not initialize Provider in constructor as activity is not set
            _provider = new PhotoProviderAndroid(activity);
        }
        return _provider;
    }
}
