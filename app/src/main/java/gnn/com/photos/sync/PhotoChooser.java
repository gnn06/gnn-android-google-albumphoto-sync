package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

class PhotoChooser {

    private final ThreadLocalRandom randomizer = ThreadLocalRandom.current();

    void chooseRandom(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename, int quantity) {
        ArrayList<Photo> chosen = chooseOneList(remote, quantity);
        chooseFull(synchronizer, local, chosen, rename);
    }

    void chooseFull(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename) {
        synchronizer.setToDownload(firstMinusSecondList(remote, local));
        if (rename != null) {
            remote = (ArrayList<Photo>) remote.clone();
            // TODO coder renommage avec une approche fonctionnelle
            Photo.renameList(remote, rename);
        }
        synchronizer.setToDelete(firstMinusSecondList(local, remote));
    }

    /**
     *
     * @param quantity should be > 0
     */
    ArrayList<Photo> chooseOneList(ArrayList<Photo> remoteLst, int quantity) {
        ArrayList<Photo> result = new ArrayList<>();
        // TODO: 23/02/21 manage that random can choose twice the same photo
        if (quantity < remoteLst.size()) {
            for (int i = 1; i <= quantity && i <= remoteLst.size(); i++) {
                int choose = randomizer.nextInt(remoteLst.size());
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