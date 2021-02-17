package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

class PhotoChooser {

    // TODO coder renommage avec une approche fonctionnelle

    void chooseRandom(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename, int quantity) {
        ArrayList<Photo> chosen = chooseOneList(remote, quantity);
        chooseFull(synchronizer, local, chosen, rename);
    }


    void chooseFull(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename) {
        synchronizer.setToDownload(firstMinusSecondList(remote, local));
        if (rename != null) {
            remote = (ArrayList<Photo>) remote.clone();
            Photo.renameList(remote, rename);
        }
        synchronizer.setToDelete(firstMinusSecondList(local, remote));
    }

    /**
     *
     * @param remoteLst
     * @param quantity should be > 0
     * @return
     */
    ArrayList<Photo> chooseOneList(ArrayList<Photo> remoteLst, int quantity) {
        ArrayList<Photo> result = new ArrayList<>();
        if (quantity < remoteLst.size()) {
            for (int i = 1; i <= quantity && i <= remoteLst.size(); i++) {
                int choose = ThreadLocalRandom.current().nextInt(remoteLst.size());
                result.add(remoteLst.get(choose));
            }
        } else {
            result = remoteLst;
        }
        return result;
    }

    /**
     * @return a copy of argument
     */
    ArrayList<Photo> firstMinusSecondList(ArrayList<Photo> remoteLst, ArrayList<Photo> localLst) {
        ArrayList<Photo> result = new ArrayList<>(remoteLst);
        result.removeAll(localLst);
        return result;
    }
}