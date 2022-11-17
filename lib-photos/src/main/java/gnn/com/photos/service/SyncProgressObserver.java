package gnn.com.photos.service;

public interface SyncProgressObserver {
    void incCurrentDownload();

    void incCurrentDelete();

    void incAlbumSize();

    void begin();

    void end();
}
