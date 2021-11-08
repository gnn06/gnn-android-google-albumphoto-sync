package gnn.com.photos.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

public abstract class PhotosRemoteService {

    public PhotosRemoteService() {

    }

    public PhotosRemoteService(File cacheFile, long cacheMaxAgeHour) {
        Cache.config(cacheFile, cacheMaxAgeHour);
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
        Logger logger = Logger.getLogger();
        logger.fine("refreshBaseUrl for " + photosToRefresh.size() + " photos");
        ArrayList<String> idsToRefresh = Photo.IdFromPhoto(photosToRefresh);
        return getPhotoProvider().getPhotosFromIds(idsToRefresh);
    }

    public void resetCache() {
        getCache().reset();
    }
}
