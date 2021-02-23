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

    // Used to Unit test
    static Cache getCache() {
        return cache;
    }

    public PhotosRemoteService() {
        cache = new Cache(null, -1);
    }

    public PhotosRemoteService(File cacheFile, long cacheMaxAge) {
        cache = new Cache(cacheFile, cacheMaxAge);
    }

    abstract public PhotoProvider getPhotoProvider();

    /**
     * Retrieve remote album list
     */
    public ArrayList<String> getAlbums() throws IOException, GoogleAuthException {
        return getPhotoProvider().getAlbums();
    }

    public ArrayList<Photo> getPhotos(String album, Synchronizer synchronizer) throws IOException, GoogleAuthException {
        ArrayList<Photo> photos = PhotosRemoteService.cache.get();
        if (photos == null) {
            photos = getPhotoProvider().getPhotosFromAlbum(album, synchronizer);
            PhotosRemoteService.cache.put(photos);
        } else {
            Log.i(PhotosRemoteService.TAG, "use cache");
        }
        synchronizer.setAlbumSize(photos.size());
        return photos;
    }

    /**
     * Refresh baseUrl and download photo
     */
    public void download(ArrayList<Photo> list, File folder, String rename, Synchronizer sync) throws IOException, GoogleAuthException {
        // baseUrl is valid during 60 minutes, refresh them before download
        list = refreshBaseUrl(list);
        new DownloadManager().download(list, folder, rename, sync);
    }

    ArrayList<Photo> refreshBaseUrl(ArrayList<Photo> photosToRefresh) throws IOException, GoogleAuthException {
        ArrayList<String> idsToRefresh = IdFromPhoto(photosToRefresh);
        return getPhotoProvider().getPhotosFromIds(idsToRefresh);
    }

    // // TODO: 23/02/21 extract into Photo package
    private ArrayList<String> IdFromPhoto(ArrayList<Photo> photos) {
        ArrayList<String> ids = new ArrayList<>();
        for (Photo item :
                photos) {
            ids.add(item.getId());
        }
        return ids;
    }

    public void resetCache() {
        if (cache != null) {
            cache.reset();
        }
    }
}
