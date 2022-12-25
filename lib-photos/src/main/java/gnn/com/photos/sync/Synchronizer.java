package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import gnn.com.photos.Photo;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;

public abstract class Synchronizer implements SyncProgressObserver {

    protected final File cacheFile;
    // 0 = update photo each time
    protected final long cacheMaxAgeHour;
    protected final File processFolder;

    public Synchronizer(File cacheFile, long cacheMaxAgeHour, File processFolder, SyncProgressObserver observer) {
        this.cacheFile = cacheFile;
        this.cacheMaxAgeHour = cacheMaxAgeHour;
        this.processFolder = processFolder;
        this.observer = observer;
    }

    // For test
    public Synchronizer(PhotosRemoteService remoteService, PhotosLocalService localService, SyncProgressObserver observer) {
        this.cacheFile = null;
        this.cacheMaxAgeHour = Cache.DELAY_ALWAYS_EXPIRE;
        this.processFolder = null;
        this.remoteService = remoteService;
        this.localService  = localService;
        this.observer = observer;
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

    public SyncData getSyncData() {
        return syncData;
    }

    public void setSyncData(SyncData syncData) {
        this.syncData = syncData;
    }

    private SyncData syncData = new SyncData();

    private SyncProgressObserver observer;

    public void setObserver(SyncProgressObserver observer) {
        this.observer = observer;
    }

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already downloaded photo from local folder
     * Determine new photo to download and photo to be deleted.
     */
    // TODO: 07/05/2019 manage updated photo if possible
    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
        sync(albumName, folder, rename, -1);
    }

    /**
     * choose one photo and download it, delete previous downloaded photo.
     */
    public void syncRandom(String albumName, File folder, String rename, int quantity) throws IOException, RemoteException {
        sync(albumName, folder, rename, quantity);
    }

    void sync(String albumName, File imageFolder, String rename, int quantity) throws IOException, RemoteException {
        // require Logger was initialized
        gnn.com.googlealbumdownloadappnougat.util.Logger logger = gnn.com.googlealbumdownloadappnougat.util.Logger.getLogger();

        begin();
        resetCurrent();
        logger.fine("get photos of album : " + albumName);
        logger.fine("download photos into folder : " + imageFolder);

        ArrayList<Photo> remote = getRemoteService().getPhotos(albumName, this);
        ArrayList<Photo> local = getLocalService().getLocalPhotos(imageFolder);

        if (quantity != -1)
            new SyncPhotoDispatcher().chooseRandom(getSyncData(), local, remote, rename, quantity);
        else
            new SyncPhotoDispatcher().chooseFull(getSyncData(), local, remote, rename);

        logger.finest("remote count = " + remote.size());
        logger.finest("local count = " + local.size());
        logger.finest("to download count = " + this.getToDownload().size());
        logger.finest("to delete count = " + this.getToDelete().size());

        remoteService.download(this.getToDownload(), imageFolder, rename, this);
        // delete AFTER download to avoid to delete everything when there is an exception during download
        localService.delete(this.getToDelete(), imageFolder, this);
        this.storeSyncTime();
        this.end();
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
        if (this.observer != null) {
            this.observer.incCurrentDownload();
        }
    }

    public void incCurrentDelete() {
        this.syncData.incCurrentDelete();
        if (this.observer != null) {
            this.observer.incCurrentDelete();
        }
    }

    public void incAlbumSize()  {
        this.syncData.incAlbumSize();
        if (this.observer != null) {
            this.observer.incAlbumSize();
        }
    }

    public void begin() {

    }

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
        this.syncData = new SyncData();
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
