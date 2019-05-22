package gnn.com.photos.model;

public class Photo {

    private String url;
    private String id;

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
