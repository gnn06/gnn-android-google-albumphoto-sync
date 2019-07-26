package gnn.com.photos.sync;

import com.google.photos.library.v1.PhotosLibraryClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.local.PhotosLocalService;
import gnn.com.photos.remote.PhotosRemoteService;

public class Synchronizer {

    DiffCalculator diffCalculator = null;
    private final Presenter.SyncTask syncTask;
    private int currentDownload;
    private int currentDelete;

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
            ArrayList remote = prs.getRemotePhotos(albumName);
            ArrayList local = pls.getLocalPhotos(folder);
            diffCalculator = new DiffCalculator(remote, local);
            pls.delete(diffCalculator.getToDelete(), folder, this);
            DownloadManager.download(diffCalculator.getToDownload(), folder, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTotalDownload() {
        return diffCalculator.getToDownload().size();
    }

    public int getTotalDelete() {
        return diffCalculator.getToDelete().size();
    }

    public int getCurrentDownload() {
        return this.currentDownload;
    }

    void incDownloadCurrent() {
        this.currentDownload += 1;
        this.syncTask.publicPublish();
    }

    public void incCurrentDelete() {
        this.currentDelete += 1;
        this.syncTask.publicPublish();
    }
}
