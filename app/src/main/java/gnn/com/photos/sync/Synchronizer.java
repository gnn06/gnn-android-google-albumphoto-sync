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

    private PhotosRemoteService getRemoteService() {
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
        PhotosRemoteService prs = getRemoteService();
        PhotosLocalService pls = getLocalService();
        System.out.println("get photos of album : " + albumName);
        System.out.println("download photos into folder : " + folder);
        this.resetCurrent();
        ArrayList<Photo> remote = prs.getPhotos(albumName, this);
        ArrayList<Photo> local = pls.getLocalPhotos(folder);
        this.toDownload = PhotoChooser.firstMinusSecond(remote, local);
        this.toDelete   = PhotoChooser.firstMinusSecond(local, remote);
        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + this.toDownload.size());
        System.out.println("to delete count = " + this.toDelete.size());
        pls.delete(this.getToDelete(), folder, this);
        prs.download(this.getToDownload(), folder, this);
    }

    /**
     * choose one photo and download it, delete previous downloaded photo.
     */
    public void chooseOne(String albumName, File folder) throws IOException, GoogleAuthException {
        PhotosRemoteService prs = getRemoteService();
        PhotosLocalService pls = getLocalService();
        this.resetCurrent();
        ArrayList<Photo> remote = prs.getPhotos(albumName, this);
        ArrayList<Photo> local = pls.getLocalPhotos(folder);
        this.toDelete   = local;
        this.toDownload = PhotoChooser.chooseOne(remote);
        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + this.toDownload.size());
        System.out.println("to delete count = " + this.toDelete.size());
        pls.delete(this.getToDelete(), folder, this);
        prs.download(this.getToDownload(), folder, this);
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

    public int getAlbumSize() {
        return this.albumSize;
    }

    public void setAlbumSize(int size) {
        this.albumSize = size;
    }

    private void resetCurrent() {
        this.currentDownload = 0;
        this.currentDelete= 0;
        this.toDownload = new ArrayList<>();
        this.toDelete = new ArrayList<>();
        this.albumSize = 0;
    }

    private ArrayList<Photo> getToDownload() {
        return toDownload;
    }

    private ArrayList<Photo> getToDelete() {
        return toDelete;
    }

    public void resetCache() {
        if (getRemoteService() != null) {
            this.remoteService.resetCache();
        }
    }
}
