package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import gnn.com.photos.service.RemoteException;
import gnn.com.util.ExpirationChecker;

/**
 * Synchronizer that check if last sync was expired
 * param :
 * - delay before sync expiration
 */
public class SynchronizerDelayed {

    protected Synchronizer synchronizer;

    final private int delay;

    public SynchronizerDelayed(int delay) {
        this.delay = delay;
    }

    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
        if (lastSyncTime == null || new ExpirationChecker(lastSyncTime, delay).isExpired()) {
            synchronizer.syncAll(albumName, folder, rename);
        } // else do noting
    }

    public void syncRandom(String albumName, File folder, String rename, int quantity) throws IOException, RemoteException {
        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
        if (lastSyncTime == null || new ExpirationChecker(lastSyncTime, delay).isExpired()) {
            synchronizer.syncRandom(albumName, folder, rename, quantity);
        } // else do noting
    }


}
