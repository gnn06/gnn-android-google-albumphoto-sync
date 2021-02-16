package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;

class SyncMethod {

    protected final Synchronizer synchronizer;
    protected final PhotosRemoteService remoteService;
    protected final PhotosLocalService localService;

    public SyncMethod(Synchronizer synchronizer, PhotosRemoteService remoteService, PhotosLocalService localService) {
        this.synchronizer = synchronizer;
        this.remoteService = remoteService;
        this.localService = localService;
    }

    void sync(String albumName, File imageFolder, String rename, int quantity) throws IOException, GoogleAuthException {
        synchronizer.resetCurrent();
        System.out.println("get photos of album : " + albumName);
        System.out.println("download photos into folder : " + imageFolder);

        ArrayList<Photo> remote = remoteService.getPhotos(albumName, synchronizer);
        ArrayList<Photo> local = localService.getLocalPhotos(imageFolder);

        if (quantity != -1)
            new PhotoChooser().chooseRandom(synchronizer, local, remote, rename, quantity);
        else
            new PhotoChooser().chooseFull(synchronizer, local, remote, rename);

        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + synchronizer.getToDownload().size());
        System.out.println("to delete count = " + synchronizer.getToDelete().size());

        remoteService.download(synchronizer.getToDownload(), imageFolder, rename, synchronizer);
        // delete AFTER download to avoid to delete everything when there is an exception during download
        localService.delete(synchronizer.getToDelete(), imageFolder, synchronizer);
        synchronizer.storeSyncTime();
    }

}