package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import gnn.com.photos.service.RemoteException;
import gnn.com.util.ExpirationChecker;

/**
 * Synchronizer that check if last sync was expired
 * param :
 * - delay before sync expiration
 */
public class SynchronizerDelayed {

    // instanciated by descendant
    protected Synchronizer synchronizer;

    final private int delay;

    public SynchronizerDelayed(int delay) {
        this.delay = delay;
    }

    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
        // require Logger was initialized
        Logger logger = Logger.getLogger("worker");

        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
        boolean expired = lastSyncTime == null || new ExpirationChecker(lastSyncTime, delay).isExpired();
        logger.info("sync expired=" + expired + " with delay=" + delay + " in minutes");
        if (expired) {
            synchronizer.syncAll(albumName, folder, rename);
        } // else do noting
    }

    public void syncRandom(String albumName, File folder, String rename, int quantity) throws IOException, RemoteException {
        // require Logger was initialized
        Logger logger = Logger.getLogger("worker");

        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
        boolean expired = lastSyncTime == null || new ExpirationChecker(lastSyncTime, delay).isExpired();
        logger.info("sync expired=" + expired + " with delay=" + delay + " in minutes");
        if (expired) {
            synchronizer.syncRandom(albumName, folder, rename, quantity);
        } // else do noting
    }


}
