package gnn.com.googlealbumdownloadappnougat.ui.presenter;

public class SyncData {

    private String album;
    private int quantity;
    private String rename;
    private String folderHuman;
    private int frequencyWallpaper;
    private int frequencySync;

    public int getFrequencyWallpaper() {
        return frequencyWallpaper;
    }

    public void setFrequencyWallpaper(int frequencyWallpaper) {
        this.frequencyWallpaper = frequencyWallpaper;
    }

    public int getFrequencySync() {
        return frequencySync;
    }

    public void setFrequencySync(int frequencySync) {
        this.frequencySync = frequencySync;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public String getFolderHuman() {
        return folderHuman;
    }

    public void setFolderHuman(String folderHuman) {
        this.folderHuman = folderHuman;
    }
}
