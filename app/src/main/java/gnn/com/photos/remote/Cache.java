package gnn.com.photos.remote;

import java.util.ArrayList;

import gnn.com.photos.model.Photo;

public class Cache {
    private ArrayList<Photo> photos;

    public ArrayList<Photo> get() {
        return this.photos;
    }

    public void put(ArrayList<Photo> photos) {
        this.photos = photos;
    }
}
