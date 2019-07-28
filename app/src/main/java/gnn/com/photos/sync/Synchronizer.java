package gnn.com.photos.sync;

import com.google.photos.library.v1.PhotosLibraryClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.local.PhotosLocalService;
import gnn.com.photos.model.Photo;
import gnn.com.photos.remote.PhotosRemoteService;

public class Synchronizer {

    private final Presenter.SyncTask syncTask;

    private int currentDownload;
    private int currentDelete;
    private ArrayList remote;
    private ArrayList local;
    private ArrayList toDownload;
    private ArrayList toDelete;

    public Synchronizer(Presenter.SyncTask syncTask) {
        this.syncTask = syncTask;
    }

    // TODO: 07/05/2019 managed updated photo if possible
    public void sync(String albumName, File folder, PhotosLibraryClient client) {
        try {
            PhotosRemoteService prs = new PhotosRemoteService(client);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    void incCurrentDownload() {
        this.currentDownload += 1;
        this.syncTask.publicPublish();
    }

    public void incCurrentDelete() {
        this.currentDelete += 1;
        this.syncTask.publicPublish();
    }

    void resetCurrent() {
        this.currentDownload = 0;
        this.currentDelete= 0;
    }

    public ArrayList<Photo> calculToDownload() {
        ArrayList result = ((ArrayList)this.remote.clone());
        result.removeAll(this.local);
        return result;
    }

    private ArrayList calculToDelete() {
        ArrayList result = ((ArrayList)this.local.clone());
        result.removeAll(this.remote);
        return result;
    }

    public ArrayList getToDownload() {
        return toDownload;
    }

    public ArrayList getToDelete() {
        return toDelete;
    }
}
