package gnn.com.photos.service;

import java.io.File;

import gnn.com.photos.sync.PersistSyncTime;
import gnn.com.photos.sync.PersistWallpaperTime;

public class CacheManager {

    private final File processFolder;

    public CacheManager(File processFolder) {
        this.processFolder = processFolder;
    }

    public void resetAll() {
        Cache.getCache().reset();
        new PersistSyncTime(processFolder).reset();
        new PersistWallpaperTime(processFolder).reset();
    }
}
