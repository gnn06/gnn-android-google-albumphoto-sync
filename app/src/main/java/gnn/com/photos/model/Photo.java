package gnn.com.photos.model;

import java.io.Serializable;

public class Photo implements Serializable {

    /**
     * Remote Url don't contain extension ".jpg"
     */
    private final String url;
    
    private final String id;

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
}
