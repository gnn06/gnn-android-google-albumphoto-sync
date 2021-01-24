package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
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

    abstract void sync (String albumName, File folder) throws IOException, GoogleAuthException;

}