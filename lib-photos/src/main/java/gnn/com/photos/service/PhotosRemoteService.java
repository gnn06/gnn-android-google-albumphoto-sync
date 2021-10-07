package gnn.com.photos.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import gnn.com.photos.Log;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

public abstract class PhotosRemoteService {

    private static final Log Log = new Log( PhotosRemoteService.class.getName() );

    private static final String TAG = "PhotosRemoteService";

    public PhotosRemoteService() {

    }

    public PhotosRemoteService(File cacheFile, long cacheMaxAge) {
        Cache.config(cacheFile, cacheMaxAge);
    }

    private Cache getCache() {
        return Cache.getCache();
    }

    abstract public PhotoProvider getPhotoProvider();

    /**
     * Retrieve remote album list
     */
    public ArrayList<String> getAlbums() throws RemoteException {
        return getPhotoProvider().getAlbums();
    }

    public ArrayList<Photo> getPhotos(String album, Synchronizer synchronizer) throws IOException, RemoteException {
        ArrayList<Photo> photos = getCache().get();
        if (photos == null) {
            photos = getPhotoProvider().getPhotosFromAlbum(album, synchronizer);
            getCache().put(photos);
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
        // require Logger was initialized
        Logger logger = Logger.getLogger("worker");
        logger.info("refreshBaseUrl for " + photosToRefresh.size() + " photos");
        ArrayList<String> idsToRefresh = Photo.IdFromPhoto(photosToRefresh);
        return getPhotoProvider().getPhotosFromIds(idsToRefresh);
    }

    public void resetCache() {
        getCache().reset();
    }
}
