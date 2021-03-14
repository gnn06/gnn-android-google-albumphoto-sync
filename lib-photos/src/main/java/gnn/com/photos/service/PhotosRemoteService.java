package gnn.com.photos.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.Log;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

public abstract class PhotosRemoteService {

    private static final Log Log = new Log( PhotosRemoteService.class.getName() );

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
    public ArrayList<String> getAlbums() throws RemoteException {
        return getPhotoProvider().getAlbums();
    }

    public ArrayList<Photo> getPhotos(String album, Synchronizer synchronizer) throws IOException, RemoteException {
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
    public void download(ArrayList<Photo> list, File folder, String rename, Synchronizer sync) throws IOException, RemoteException {
        // baseUrl is valid during 60 minutes, refresh them before download
        list = refreshBaseUrl(list);
        new DownloadManager().download(list, folder, rename, sync);
    }

    ArrayList<Photo> refreshBaseUrl(ArrayList<Photo> photosToRefresh) throws RemoteException {
        ArrayList<String> idsToRefresh = Photo.IdFromPhoto(photosToRefresh);
        return getPhotoProvider().getPhotosFromIds(idsToRefresh);
    }

    public void resetCache() {
        if (cache != null) {
            cache.reset();
        }
    }
}
