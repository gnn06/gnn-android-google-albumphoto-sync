package gnn.com.photos.sync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import gnn.com.photos.Log;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public abstract class Synchronizer implements SyncData {

    private static final gnn.com.photos.Log Log = new Log( Cache.class.getName() );

    protected final File cacheFile;
    protected final long cacheMaxAge;
    protected final File processFolder;

    public Synchronizer(File cacheFile, long cacheMaxAge, File processFolder) {
        this.cacheFile = cacheFile;
        this.cacheMaxAge = cacheMaxAge;
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

    // TODO maybe move field into SyncData
    private int currentDownload;
    private int currentDelete;
    private ArrayList<Photo> toDownload;
    private ArrayList<Photo> toDelete;
    private int albumSize = 0;

    /**
     * Main method.
     * Synchronize local folder with given album name.
     * Retrieve photo list from Google, retrieve already donwloaded photo from local folder
     * Determine new photo to download and photo to be deleled.
     */
    // TODO: 07/05/2019 managed updated photo if possible
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
        return this.currentDownload;
    }

    public int getCurrentDelete() {
        return this.currentDelete;
    }

    public void incCurrentDownload() {
        this.currentDownload += 1;
    }

    public void incCurrentDelete() {
        this.currentDelete += 1;
    }

    public void incAlbumSize()  {
        this.albumSize += 1;
    };

    public void setToDownload(ArrayList<Photo> toDownload) {
        this.toDownload = toDownload;
    }

    ArrayList<Photo> getToDownload() {
        return toDownload;
    }

    public void setToDelete(ArrayList<Photo> toDelete) {
        this.toDelete = toDelete;
    }

    ArrayList<Photo> getToDelete() {
        return toDelete;
    }

    public int getAlbumSize() {
        return this.albumSize;
    }

    public void setAlbumSize(int size) {
        this.albumSize = size;
    }

    public void resetCache() {
        if (getRemoteService() != null) {
            this.remoteService.resetCache();
        }
    }

    void resetCurrent() {
        this.currentDownload = 0;
        this.currentDelete= 0;
        this.toDownload = new ArrayList<>();
        this.toDelete = new ArrayList<>();
        this.albumSize = 0;
    }

    void storeSyncTime() throws IOException {
        if (processFolder != null) {
            File file = new File(processFolder, "last_sync");
            System.out.println(file.getAbsolutePath());
            Log.i("SyncMethod", "write " + file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            // TODO get current time and write it
            writer.write("sync time");
            writer.close();
        }
    }

    /**
     * @return format = MM/dd/yyyy HH:mm:ss
     *         null if no previous sync
     */
    public String retrieveLastSyncTime() {
        String stringLastModified = null;
        if (processFolder != null) {
            File file = new File(processFolder, "last_sync");
            if (file.exists()) {
                DateFormat sdf = SimpleDateFormat.getInstance();
                stringLastModified = sdf.format(file.lastModified());
            }
        }
        return stringLastModified;
    }
}
