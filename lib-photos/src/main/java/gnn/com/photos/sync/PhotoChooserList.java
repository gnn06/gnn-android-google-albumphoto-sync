package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.Random;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;

/**
 * Choose a photo from a given list. Avoid to choose previously chosen photos.
 * Use by SyncMethod and PhotoWallpaper
 */
public class PhotoChooserList {

    // randomizer must not be final to be mocked by mockito
    // test randomizer instancié à chaque appel

    void chooseRandom(SyncData synchronizer, ArrayList<Photo> local, ArrayList<Photo> remote, String rename, int quantity) {
        ArrayList<Photo> chosen = chooseOneList(remote, quantity, local);
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
     * try to choose photo that was not previously chosen
     * @param quantity should be > 0
     * @param previousPhotos
     */
    public ArrayList<Photo> chooseOneList(ArrayList<Photo> remoteLst, int quantity, ArrayList<Photo> previousPhotos) {
        Logger logger = Logger.getLogger();
        ArrayList<Photo> result = new ArrayList<>();
        if (quantity < remoteLst.size()) {
            if (previousPhotos != null) {
                // first choose photo to avoid previous
                ArrayList<Photo> photos = firstMinusSecondList((ArrayList<Photo>) remoteLst.clone(), previousPhotos);
                while (result.size() < quantity && result.size() < photos.size()) {
                    int choose = new Random().nextInt(photos.size());
                    logger.finest("chosen " + choose + " in " + photos.size());
                    // if choose an already choosen element
                    if (!result.contains(photos.get(choose))) {
                        result.add(photos.get(choose));
                    }
                }
            }
            // then choose photo into all list
            while (result.size() < quantity) {
                int choose = new Random().nextInt(remoteLst.size());
                logger.finest("chosen " + choose + " in " + remoteLst.size());
                // if choose an already chosen element
                if (!result.contains(remoteLst.get(choose))) {
                    result.add(remoteLst.get(choose));
                }
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