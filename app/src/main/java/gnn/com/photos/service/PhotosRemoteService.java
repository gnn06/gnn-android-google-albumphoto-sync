package gnn.com.photos.service;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.DownloadManager;
import gnn.com.photos.sync.Synchronizer;

public abstract class PhotosRemoteService {

    private static final String TAG = "PhotosRemoteService";

    /**
     * As class var, cache is common between PhotosRemoteService instances.
     */
    private static Cache cache;
    private final PhotoProvider photoProvider;

    // Used to Unit test
    static Cache getCache() {
        return cache;
    }

    public PhotosRemoteService() {
        cache = new Cache(null, -1);
        photoProvider = getPhotoProvider();
    }

    public PhotosRemoteService(File cacheFile, long cacheMaxAge) {
        cache = new Cache(cacheFile, cacheMaxAge);
        photoProvider = getPhotoProvider();
    }

    abstract public PhotoProvider getPhotoProvider();

    /**
     * Retrieve remote album list
     */
    public ArrayList<String> getAlbums() throws IOException, GoogleAuthException {
        return photoProvider.getAlbums();
    }

    public ArrayList<Photo> getPhotos(String album, Synchronizer synchronizer) throws IOException, GoogleAuthException {
        ArrayList<Photo> photos = PhotosRemoteService.cache.get();
        if (photos == null) {
            photos = photoProvider.getPhotos(album, synchronizer);
            PhotosRemoteService.cache.put(photos);
        } else {
            Log.i(PhotosRemoteService.TAG, "use cache");
        }
        synchronizer.setAlbumSize(photos.size());
        return photos;
    }

    public void download(ArrayList<Photo> list, File folder, String rename, Synchronizer sync) throws IOException {
        // TODO: 18/02/21 refresh baseUrl is expired
        // baseUrl is valid during 60 minutes, refresh them before download
        new DownloadManager().download(list, folder, rename, sync);
    }

    public void resetCache() {
        if (cache != null) {
            cache.reset();
        }
    }
}
