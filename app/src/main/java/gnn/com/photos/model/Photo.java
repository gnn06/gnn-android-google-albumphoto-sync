package gnn.com.photos.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {

    /**
     * Remote Url don't contain extension ".jpg"
     */
    private final String url;
    
    private final String id;

    /**
     * rename Photos in list creating new Photos
     */
    public static void renameList(ArrayList<Photo> list, String rename) {
        for (int i = 0; i < list.size(); i++) {
            Photo photo = list.get(i);
            list.set(i, new Photo(photo.getUrl(), rename + (i + 1)));
        }
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public Photo(String url, String id) {
        this.url = url;
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        return this.id.equals(((Photo)other).id);
    }

    @NonNull
    @Override
    public String toString() {
        return id;
    }
}
