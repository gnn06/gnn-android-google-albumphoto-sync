package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosRemoteService;

public abstract class Synchronizer {

    protected final File fileCache;

    public Synchronizer(File cacheFile) {
        this.fileCache = cacheFile;
    }

    private PhotosRemoteService remoteService;
    private PhotosLocalService localService;

    PhotosRemoteService getRemoteService() {
        if (this.remoteService == null) {
            this.remoteService = getRemoteServiceImpl();
        }
        return this.remoteService;
    }

    abstract protected PhotosRemoteService getRemoteServiceImpl();

    protected PhotosLocalService getLocalService() {
        if (this.localService == null) {
            this.localService = new PhotosLocalService();
        }
        return this.localService;
    }

    private int currentDownload;
    private int currentDelete;
    private ArrayList<Photo> toDownload;
    private ArrayList<Photo> toDelete;
    protected int albumSize = 0;

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already donwloaded photo from local folder
     * Determine new photo to download and photo to be deleled.
     */
    // TODO: 07/05/2019 managed updated photo if possible
    public void sync(String albumName, File folder) throws IOException, GoogleAuthException {
        new SyncFull(this, getRemoteService(), getLocalService())
                .sync(albumName, folder);
    }

    /**
     * choose one photo and download it, delete previous downloaded photo.
     */
    public void chooseOne(String albumName, File folder) throws IOException, GoogleAuthException {
        new SyncRandom(this, getRemoteService(), this.getLocalService())
                .sync(albumName, folder);
    }

    public int getTotalDownload() {
        return this.getToDownload().size();
    }

    public int getTotalDelete() {
        return this.getToDelete().size();
    }

    public int getCurrentDownload() {
        return this.currentDownload;
    }

    public int getCurrentDelete() {
        return this.currentDelete;
    }

    protected void incCurrentDownload() {
        this.currentDownload += 1;
    }

    public void incCurrentDelete() {
        this.currentDelete += 1;
    }

    abstract public void incAlbumSize();

    void setToDownload(ArrayList<Photo> toDownload) {
        this.toDownload = toDownload;
    }

    ArrayList<Photo> getToDownload() {
        return toDownload;
    }

    void setToDelete(ArrayList<Photo> toDelete) {
        this.toDelete = toDelete;
    }

    ArrayList<Photo> getToDelete() {
        return toDelete;
    }

    public int getAlbumSize() {
        return this.albumSize;
    }

    public void setAlbumSize(int size) {
        this.albumSize = size;
    }

    void resetCurrent() {
        this.currentDownload = 0;
        this.currentDelete= 0;
        this.toDownload = new ArrayList<>();
        this.toDelete = new ArrayList<>();
        this.albumSize = 0;
    }

    public void resetCache() {
        if (getRemoteService() != null) {
            this.remoteService.resetCache();
        }
    }
}
