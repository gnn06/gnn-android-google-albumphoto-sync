package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;

public class SyncMethod {
    private final Synchronizer synchronizer;

    public SyncMethod(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already donwloaded photo from local folder
     * Determine new photo to download and photo to be deleled.
     */
    // TODO: 07/05/2019 managed updated photo if possible
    public void sync(String albumName, File folder) throws IOException, GoogleAuthException {
        PhotosRemoteService prs = synchronizer.getRemoteService();
        PhotosLocalService pls = synchronizer.getLocalService();
        System.out.println("get photos of album : " + albumName);
        System.out.println("download photos into folder : " + folder);
        synchronizer.resetCurrent();
        ArrayList<Photo> remote = prs.getPhotos(albumName, synchronizer);
        ArrayList<Photo> local = pls.getLocalPhotos(folder);
        synchronizer.setToDownload(PhotoChooser.firstMinusSecond(remote, local));
        synchronizer.setToDelete(PhotoChooser.firstMinusSecond(local, remote));
        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + synchronizer.getToDownload().size());
        System.out.println("to delete count = " + synchronizer.getToDelete().size());
        pls.delete(synchronizer.getToDelete(), folder, synchronizer);
        prs.download(synchronizer.getToDownload(), folder, synchronizer);
    }

    /**
     * choose one photo and download it, delete previous downloaded photo.
     */
    public void chooseOne(String albumName, File folder) throws IOException, GoogleAuthException {
        PhotosRemoteService prs = synchronizer.getRemoteService();
        PhotosLocalService pls = synchronizer.getLocalService();
        synchronizer.resetCurrent();
        ArrayList<Photo> remote = prs.getPhotos(albumName, synchronizer);
        ArrayList<Photo> local = pls.getLocalPhotos(folder);
        synchronizer.setToDelete(local);
        synchronizer.setToDownload(PhotoChooser.chooseOne(remote));
        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + synchronizer.getToDownload().size());
        System.out.println("to delete count = " + synchronizer.getToDelete().size());
        pls.delete(synchronizer.getToDelete(), folder, synchronizer);
        prs.download(synchronizer.getToDownload(), folder, synchronizer);
    }
}