package gnn.com.photos;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {

    private static final long serialVersionUID = 1234567L;

    /**
     * Remote Url don't contain extension ".jpg"
     */
    private final String url;
    
    private final String id;

    public Photo(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    /**
     * rename Photos in list creating new Photos
     */
    public static void renameList(ArrayList<Photo> list, String rename) {
        for (int i = 0; i < list.size(); i++) {
            Photo photo = list.get(i);
            list.set(i, new Photo(photo.getUrl(), rename + (i + 1)));
        }
    }

    public static ArrayList<String> IdFromPhoto(ArrayList<Photo> photos) {
        ArrayList<String> ids = new ArrayList<>();
        for (Photo item : photos) {
            ids.add(item.getId());
        }
        return ids;
    }

    @Override
    public boolean equals(Object other) {
        return this.id.equals(((Photo)other).id);
    }

    @Override
    public String toString() {
        return id;
    }

    public String getPhotoLocalFileName() {
        return getId() + getFileExtension();
    }

    public static String getFileExtension() {
        // TODO: 06/05/2019 manage file extension from mimeType
        return ".jpg";
    }
}
