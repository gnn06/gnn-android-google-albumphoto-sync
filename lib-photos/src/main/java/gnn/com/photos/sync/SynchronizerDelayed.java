package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;
import gnn.com.util.ExpirationChecker;

/**
 * Synchronizer that check if last sync was expired
 * param :
 * - delay before sync expiration
 */
public class SynchronizerDelayed {

    // instanciated by descendant
    protected Synchronizer synchronizer;

    public static final int DELAY_ALWAYS_SYNC = 0;
    public static final int DELAY_NEVER_SYNC = Integer.MAX_VALUE;

    /**
     * 0 = no cache, sync each times
     * MAX = cache never expire, never sync
     */
    final private int delayMinute;

    /**
     * @param delayMinute 0 = always sync, MAX_VALUE = never sync
     */
    public SynchronizerDelayed(int delayMinute) {
        this.delayMinute = delayMinute;
    }

    // For test
    public SynchronizerDelayed(int delayMinute, Synchronizer sync) {
        this.synchronizer = sync;
        this.delayMinute = delayMinute;
    }

//    public void syncAll(String albumName, File folder, String rename) throws IOException, RemoteException {
//        // require Logger was initialized
//        Logger logger = Logger.getLogger();
//
//        Date lastSyncTime = synchronizer.retrieveLastSyncTime();
//        boolean expired = lastSyncTime == null || new ExpirationChecker(lastSyncTime, delayMinute).isExpired();
//        logger.info("sync expired=" + expired + " with delay=" + delayMinute + " in minutes");
//        if (expired) {
//            synchronizer.syncAll(albumName, folder, rename);
//        } // else do noting
//    }

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


    public void setObserver(SyncProgressObserver observer) {
        this.synchronizer.setObserver(observer);
    }
}
