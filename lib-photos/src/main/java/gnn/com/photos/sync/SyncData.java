package gnn.com.photos.sync;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Objects;

import gnn.com.photos.Photo;

public class SyncData {

    public SyncData() {
        this.albumSize = 0;
        this.deleteCount = 0;
        this.downloadCount = 0;
        this.toDownload = null;
        this.toDelete = null;
        this.currentDownload = 0;
        this.currentDelete = 0;
    }

    public SyncData(ArrayList<Photo> toDownload, ArrayList<Photo> toDelete) {
        this.albumSize = 0;
        this.deleteCount = 0;
        this.downloadCount = 0;
        this.toDownload = toDownload;
        this.toDelete = toDelete;
        this.currentDownload = 0;
        this.currentDelete = 0;
    }

    // For Test
    public SyncData(int albumSize, int deleteCount, int downloadCount, ArrayList<Photo> downloadLst, ArrayList<Photo> deleteLst, int currentDownload, int currentDelete) {
        this.albumSize = albumSize;
        this.deleteCount = deleteCount;
        this.downloadCount = downloadCount;
        this.toDownload = downloadLst;
        this.toDelete = deleteLst;
        this.currentDownload = currentDownload;
        this.currentDelete = currentDelete;
    }

    @Expose private int albumSize;

    @Expose private int deleteCount;

    @Expose private int downloadCount;

    private ArrayList<Photo> toDownload;
    private ArrayList<Photo> toDelete;

    private int currentDownload;
    private int currentDelete;

    public ArrayList<Photo> getToDownload() {
        return toDownload;
    }

    public void setToDownload(ArrayList<Photo> toDownload) {
        this.toDownload = toDownload;
        this.downloadCount = toDownload.size();
    }

    public ArrayList<Photo> getToDelete() {
        return toDelete;
    }

    public void setToDelete(ArrayList<Photo> toDelete) {
        this.toDelete = toDelete;
        this.deleteCount = toDelete.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyncData syncData = (SyncData) o;
        return albumSize == syncData.albumSize && deleteCount == syncData.deleteCount && downloadCount == syncData.downloadCount && Objects.equals(toDownload, syncData.toDownload) && Objects.equals(toDelete, syncData.toDelete);
    }

    public void incAlbumSize() {
        this.albumSize++;
    }

    public int getAlbumSize() {
        return albumSize;
    }

    public void setAlbumSize(int albumSize) {
        this.albumSize = albumSize;
    }

    public int getCurrentDownload() {
        return currentDownload;
    }

    public void incCurrentDownload() {
        this.currentDownload++;
    }

    public int getCurrentDelete() {
        return currentDelete;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void incCurrentDelete() {
        this.currentDelete++;
    }
}
