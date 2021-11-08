package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
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

    final private int delayMinute;

    public SynchronizerDelayed(int delayMinute) {
        this.delayMinute = delayMinute;
    }

    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
        // require Logger was initialized
        Logger logger = Logger.getLogger();

        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
        boolean expired = lastSyncTime == null || new ExpirationChecker(lastSyncTime, delayMinute).isExpired();
        logger.info("sync expired=" + expired + " with delay=" + delayMinute + " in minutes");
        if (expired) {
            synchronizer.syncAll(albumName, folder, rename);
        } // else do noting
    }

    public void syncRandom(String albumName, File folder, String rename, int quantity) throws IOException, RemoteException {
        // require Logger was initialized
        Logger logger = Logger.getLogger();

        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
        boolean expired = lastSyncTime == null || new ExpirationChecker(lastSyncTime, delayMinute).isExpired();
        logger.info("sync expired=" + expired + " with delay=" + delayMinute + " in minutes");
        if (expired) {
            synchronizer.syncRandom(albumName, folder, rename, quantity);
        } // else do noting
    }


}
