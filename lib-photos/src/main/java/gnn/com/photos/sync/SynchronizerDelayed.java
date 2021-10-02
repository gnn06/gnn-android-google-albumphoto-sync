package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;

import gnn.com.photos.service.RemoteException;

public class SynchronizerDelayed {

    protected Synchronizer synchronizer;

    final private long delay;

    public SynchronizerDelayed(long delay) {
        this.delay = delay;
    }

    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
        String lastSyncTime = synchronizer.retrieveLastSyncTime();
        if (new ExpirationChecker(delay).isExpired(lastSyncTime)) {
            synchronizer.syncAll(albumName, folder, rename);
        } // else do noting
    }

    public void syncRandom(String albumName, File folder, String rename, int quantity) throws IOException, RemoteException {
        String lastSyncTime = synchronizer.retrieveLastSyncTime();
        if (new ExpirationChecker(delay).isExpired(lastSyncTime)) {
            synchronizer.syncRandom(albumName, folder, rename, quantity)
        } // else do noting
    }


}
