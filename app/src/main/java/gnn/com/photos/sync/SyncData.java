package gnn.com.photos.sync;

import java.util.ArrayList;

import gnn.com.photos.model.Photo;

interface SyncData {

    void setToDownload(ArrayList<Photo> chosen);

    void setToDelete(ArrayList<Photo> local);
}
