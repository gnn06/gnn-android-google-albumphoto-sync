package gnn.com.photos.sync;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

public class RemoteSelector {

    static ArrayList<Photo> chooseOneToDownload(ArrayList<Photo> remoteLst) {
        ArrayList<Photo> result = new ArrayList<>();
        int choose = ThreadLocalRandom.current().nextInt(remoteLst.size());
        result.add(remoteLst.get(choose));
        return result;
    }

    static ArrayList<Photo> calculToDownload(ArrayList<Photo> remoteLst, ArrayList<Photo> localLst) {
        ArrayList<Photo> result = new ArrayList<>(remoteLst);
        result.removeAll(localLst);
        return result;
    }
}