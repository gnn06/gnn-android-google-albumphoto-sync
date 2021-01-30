package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

class PhotoChooser {

    static void chooseRandom(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote) {
        synchronizer.setToDelete(local);
        ArrayList<Photo> chosen = PhotoChooser.chooseOneList(remote);
        synchronizer.setToDownload(chosen);
    }

    static void chooseFull(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote) {
        synchronizer.setToDownload(PhotoChooser.firstMinusSecondList(remote, local));
        synchronizer.setToDelete(PhotoChooser.firstMinusSecondList(local, remote));
    }

    static ArrayList<Photo> chooseOneList(ArrayList<Photo> remoteLst) {
        ArrayList<Photo> result = new ArrayList<>();
        int choose = ThreadLocalRandom.current().nextInt(remoteLst.size());
        result.add(remoteLst.get(choose));
        return result;
    }

    static ArrayList<Photo> firstMinusSecondList(ArrayList<Photo> remoteLst, ArrayList<Photo> localLst) {
        ArrayList<Photo> result = new ArrayList<>(remoteLst);
        result.removeAll(localLst);
        return result;
    }
}