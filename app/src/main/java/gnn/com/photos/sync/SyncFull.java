package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;

class SyncFull extends SyncMethod {

    public SyncFull(Synchronizer synchronizer,
                    PhotosRemoteService remoteService,
                    PhotosLocalService localService, File processFolder) {
        super(synchronizer, remoteService, localService, processFolder);
    }

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already donwloaded photo from local folder
     * Determine new photo to download and photo to be deleled.
     */
    // TODO: 07/05/2019 managed updated photo if possible
    void syncImpl(String albumName, File folder) throws IOException, GoogleAuthException {
        System.out.println("get photos of album : " + albumName);
        System.out.println("download photos into folder : " + folder);
        ArrayList<Photo> remote = remoteService.getPhotos(albumName, synchronizer);
        ArrayList<Photo> local = localService.getLocalPhotos(folder);
        synchronizer.setToDownload(PhotoChooser.firstMinusSecond(remote, local));
        synchronizer.setToDelete(PhotoChooser.firstMinusSecond(local, remote));
        System.out.println("remote count = " + remote.size());
        System.out.println("local count = " + local.size());
        System.out.println("to download count = " + synchronizer.getToDownload().size());
        System.out.println("to delete count = " + synchronizer.getToDelete().size());
        localService.delete(synchronizer.getToDelete(), folder, synchronizer);
        remoteService.download(synchronizer.getToDownload(), folder, synchronizer);
    }
}
