package gnn.com.photos.sync;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.util.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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

    void sync(String albumName, File imageFolder, File processFolder) throws IOException, GoogleAuthException {
        syncImpl(albumName, imageFolder);
        storeSyncTime(processFolder);
    }

    abstract void syncImpl (String albumName, File folder) throws IOException, GoogleAuthException;

    private void storeSyncTime(File folder) throws IOException {
        if (folder != null) {
            File file = new File(folder, "last_sync");
            System.out.println(file.getAbsolutePath());
            Log.i("SyncMethod", "write " + file.getAbsolutePath());
            FileWriter writer = new FileWriter(file);
            // TODO get current time and write it
            writer.write("sync time");
            writer.close();
        }
    }

}