package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

class PhotoChooser {

    static ArrayList<Photo> chooseOne(ArrayList<Photo> remoteLst) {
        ArrayList<Photo> result = new ArrayList<>();
        int choose = ThreadLocalRandom.current().nextInt(remoteLst.size());
        result.add(remoteLst.get(choose));
        return result;
    }

    static ArrayList<Photo> firstMinusSecond(ArrayList<Photo> remoteLst, ArrayList<Photo> localLst) {
        ArrayList<Photo> result = new ArrayList<>(remoteLst);
        result.removeAll(localLst);
        return result;
    }
}