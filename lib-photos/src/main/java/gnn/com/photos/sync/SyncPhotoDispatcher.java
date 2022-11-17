package gnn.com.photos.sync;

import java.util.ArrayList;

import gnn.com.photos.Photo;

/**
 * Given a remote photo list and  a local photo list, determine wich to download and to delete and put them into given SyncData
 */

class SyncPhotoDispatcher {

    final private PhotoChooserList photoChooserList;

    public SyncPhotoDispatcher() {
        this.photoChooserList = new PhotoChooserList();
    }

    public SyncPhotoDispatcher(PhotoChooserList photoChooserList) {
        this.photoChooserList = photoChooserList;
    }

    void chooseRandom(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename, int quantity) {
        ArrayList<Photo> chosen = photoChooserList.chooseOneList(remote, quantity, local);
        chooseFull(synchronizer, local, chosen, rename);
    }

    void chooseFull(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename) {
        synchronizer.setToDownload(photoChooserList.firstMinusSecondList(remote, local));
        if (rename != null) {
            remote = (ArrayList<Photo>) remote.clone();
            Photo.renameList(remote, rename);
        }
        synchronizer.setToDelete(photoChooserList.firstMinusSecondList(local, remote));
    }
}
