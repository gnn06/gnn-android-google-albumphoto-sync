package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;

public abstract class Synchronizer implements SyncData, SyncProgressObserver {

    protected final File cacheFile;
    // 0 = update photo each time
    protected final long cacheMaxAgeHour;
    protected final File processFolder;

    public Synchronizer(File cacheFile, long cacheMaxAgeHour, File processFolder) {
        this.cacheFile = cacheFile;
        this.cacheMaxAgeHour = cacheMaxAgeHour;
        this.processFolder = processFolder;
    }

    private PhotosRemoteService remoteService;
    private PhotosLocalService localService;

    PhotosRemoteService getRemoteService() {
        if (this.remoteService == null) {
            this.remoteService = getRemoteServiceImpl();
        }
        return this.remoteService;
    }

    abstract protected PhotosRemoteService getRemoteServiceImpl();

    protected PhotosLocalService getLocalService() {
        if (this.localService == null) {
            this.localService = new PhotosLocalService();
        }
        return this.localService;
    }

    public Temp getSyncData() {
        return syncData;
    }

    public void setSyncData(Temp syncData) {
        this.syncData = syncData;
    }

    private Temp syncData = new Temp();

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already downloaded photo from local folder
     * Determine new photo to download and photo to be deleted.
     */
    // TODO: 07/05/2019 manage updated photo if possible
    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
        new SyncMethod(this, getRemoteService(), getLocalService())
                .sync(albumName, folder, rename, -1);
    }

    /**
     * choose one photo and download it, delete previous downloaded photo.
     */
    public void syncRandom(String albumName, File folder, String rename, int quantity) throws IOException, RemoteException {
        new SyncMethod(this, getRemoteService(), this.getLocalService())
                .sync(albumName, folder, rename, quantity);
    }

    public int getTotalDownload() {
        return this.getToDownload().size();
    }

    public int getTotalDelete() {
        return this.getToDelete().size();
    }

    public int getCurrentDownload() {
        return this.syncData.getCurrentDownload();
    }

    public int getCurrentDelete() {
        return this.syncData.getCurrentDelete();
    }

    public void incCurrentDownload() {
        this.syncData.incCurrentDownload();
    }

    public void incCurrentDelete() {
        this.syncData.incCurrentDelete();
    }

    public void incAlbumSize()  {
        this.syncData.incAlbumSize();
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    public void setToDownload(ArrayList<Photo> toDownload) {
        this.syncData.setToDownload(toDownload);
    }

    ArrayList<Photo> getToDownload() {
        return this.syncData.getToDownload();
    }

    public void setToDelete(ArrayList<Photo> toDelete) {
        this.syncData.setToDelete(toDelete);
    }

    ArrayList<Photo> getToDelete() {
        return this.syncData.getToDelete();
    }

    public int getAlbumSize() {
        return this.syncData.getAlbumSize();
    }

    public void setAlbumSize(int size) {
        this.syncData.setAlbumSize(size);
    }

    public void resetCache() {
        if (getRemoteService() != null) {
            this.remoteService.resetCache();
        }
    }

    void resetCurrent() {
        this.syncData = new Temp();
    }

    void storeSyncTime() throws IOException {
        Logger logger = Logger.getLogger("worker");
        logger.finest("write " + (processFolder != null ? processFolder.getAbsolutePath() : "null")) ;
        new PersistSyncTime(processFolder).storeTimeWithResult(this.syncData);
    }

    /**
     * @return format = MM/dd/yyyy HH:mm:ss
     *         null if no previous sync
     */
    public Date retrieveLastSyncTime() {
        return new PersistSyncTime(processFolder).retrieveTime();
    }
}
