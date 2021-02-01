package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;

import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;

abstract class SyncMethod {

    protected final Synchronizer synchronizer;
    protected final PhotosRemoteService remoteService;
    protected final PhotosLocalService localService;

    public SyncMethod(Synchronizer synchronizer, PhotosRemoteService remoteService, PhotosLocalService localService) {
        this.synchronizer = synchronizer;
        this.remoteService = remoteService;
        this.localService = localService;
    }

    void sync(String albumName, File imageFolder, int quantity) throws IOException, GoogleAuthException {
        synchronizer.resetCurrent();
        syncImpl(albumName, imageFolder, quantity);
        synchronizer.storeSyncTime();
    }

    abstract void syncImpl(String albumName, File folder, int quantity) throws IOException, GoogleAuthException;

}