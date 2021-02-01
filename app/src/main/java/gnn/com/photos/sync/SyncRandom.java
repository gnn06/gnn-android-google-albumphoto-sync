package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;

class SyncRandom extends SyncMethod {

    public SyncRandom(Synchronizer synchronizer,
                      PhotosRemoteService remoteService,
                      PhotosLocalService localService) {
        super(synchronizer, remoteService, localService);
    }

    /**
     * choose one photo and download it, delete previous downloaded photo.
     */
    void syncImpl(String albumName, File folder, int quantity) throws IOException, GoogleAuthException {
        ArrayList<Photo> remote = remoteService.getPhotos(albumName, synchronizer);
        ArrayList<Photo> local = localService.getLocalPhotos(folder);
        new PhotoChooser().chooseRandom(synchronizer, local, remote, quantity);
        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + synchronizer.getToDownload().size());
        System.out.println("to delete count = " + synchronizer.getToDelete().size());
        localService.delete(synchronizer.getToDelete(), folder, synchronizer);
        remoteService.download(synchronizer.getToDownload(), folder, synchronizer);
    }
}
