package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.local.PhotosLocalService;
import gnn.com.photos.model.Photo;
import gnn.com.photos.remote.PhotosRemoteService;

public abstract class Synchronizer {

    private int currentDownload;
    private int currentDelete;
    private ArrayList<Photo> remote;
    private ArrayList<Photo> local;
    private ArrayList<Photo> toDownload;
    private ArrayList<Photo> toDelete;

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already donwloaded photo from local folder
     * Determine new photo to download and photo to be deleled.
     */
    // TODO: 07/05/2019 managed updated photo if possible
    public void sync(String albumName, File folder) throws IOException, GoogleAuthException {
        PhotosRemoteService prs = getRemoteService();
        PhotosLocalService pls = new PhotosLocalService();
        System.out.println("get photos of album : " + albumName);
        System.out.println("download photos into folder : " + folder);
        this.resetCurrent();
        this.remote = prs.getRemotePhotos(albumName);
        this.local = pls.getLocalPhotos(folder);
        this.toDownload = calculToDownload();
        this.toDelete   = calculToDelete();
        System.out.println("remote count = " + this.remote.size());
        System.out.println("local count = " + this.local.size());
        System.out.println("to download count = " + this.toDownload.size());
        System.out.println("to delete count = " + this.toDelete.size());
        pls.delete(this.getToDelete(), folder, this);
        DownloadManager.download(this.getToDownload(), folder, this);
    }

    abstract protected PhotosRemoteService getRemoteService();

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

    void resetCurrent() {
        this.currentDownload = 0;
        this.currentDelete= 0;
    }

    public ArrayList<Photo> calculToDownload() {
        ArrayList<Photo> result = new ArrayList<>(this.remote);
        result.removeAll(this.local);
        return result;
    }

    private ArrayList<Photo> calculToDelete() {
        ArrayList<Photo> result = new ArrayList<>(this.local);
        result.removeAll(this.remote);
        return result;
    }

    public ArrayList<Photo> getToDownload() {
        return toDownload;
    }

    public ArrayList<Photo> getToDelete() {
        return toDelete;
    }
}
