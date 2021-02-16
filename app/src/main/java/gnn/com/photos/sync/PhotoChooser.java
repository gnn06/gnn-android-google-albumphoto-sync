package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

class PhotoChooser {

    void chooseRandom(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename, int quantity) {
        ArrayList<Photo> chosen = chooseOneList(remote, quantity);
        // download choosen except ones already downloaded (one already in local)
        synchronizer.setToDownload(firstMinusSecondList(chosen, local));
        // delete all except choosen
        if (rename != null) {
            chosen = (ArrayList<Photo>) chosen.clone();
            for (int i = 0; i < chosen.size(); i++) {
                chosen.set(i, new Photo(chosen.get(i).getUrl(), "name" + (i + 1)));
            }
        }
        synchronizer.setToDelete(firstMinusSecondList(local, chosen));
    }


    void chooseFull(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename) {
        synchronizer.setToDownload(firstMinusSecondList(remote, local));
        if (rename != null) {
            remote = (ArrayList<Photo>) remote.clone();
            for (int i = 0; i < remote.size(); i++) {
                remote.set(i, new Photo(remote.get(i).getUrl(), "name" + (i + 1)));
            }
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