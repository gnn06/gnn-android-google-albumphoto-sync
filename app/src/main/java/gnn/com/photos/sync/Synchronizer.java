package gnn.com.photos.sync;

import com.google.photos.library.v1.PhotosLibraryClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.local.PhotosLocalService;
import gnn.com.photos.remote.PhotosRemoteService;

public class Synchronizer {

    // TODO: 07/05/2019 managed updated photo if possible
    public void sync(String albumName, File folder, PhotosLibraryClient client, Presenter.SyncTask syncTask) {
        DiffCalculator diffCalculator = null;
        try {
            PhotosRemoteService prs = new PhotosRemoteService(client);
            PhotosLocalService pls = new PhotosLocalService();
            System.out.println("get photos of album : " + albumName);
            System.out.println("download photos into folder : " + folder);
            ArrayList remote = prs.getRemotePhotos(albumName);
            ArrayList local = pls.getLocalPhotos(folder);
            diffCalculator = new DiffCalculator(remote, local, syncTask);
            pls.delete(diffCalculator.getToDelete(), folder, syncTask);
            DownloadManager.download(diffCalculator.getToDownload(), folder, syncTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
