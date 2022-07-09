package gnn.com.googlealbumdownloadappnougat;

import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.WallpaperSchedulerWithPermission;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ServiceLocator {

    private static ServiceLocator _ServiceLocator;
    private WallpaperSchedulerWithPermission wallpaperSchedulerWithPermission;
    private WallpaperScheduler wallpaperScheduler;
    private SyncScheduler syncScheduler;
    private PersistPrefMain persistMain;

    public static ServiceLocator getInstance() {
        if (_ServiceLocator == null) {
            _ServiceLocator = new ServiceLocator();
        }
        return _ServiceLocator;
    }

    public WallpaperScheduler getWallpaperScheduler() {
        return wallpaperScheduler;
    }

    public void setWallpaperScheduler(WallpaperScheduler wallpaperScheduler) {
        if (this.wallpaperScheduler == null) {
            this.wallpaperScheduler = wallpaperScheduler;
        }
    }

    public WallpaperSchedulerWithPermission getWallpaperSchedulerWithPermission() {
        return wallpaperSchedulerWithPermission;
    }

    public void setWallpaperSchedulerWithPermission(WallpaperSchedulerWithPermission wallpaperSchedulerWithPermission) {
        if (this.wallpaperSchedulerWithPermission == null) {
            this.wallpaperSchedulerWithPermission = wallpaperSchedulerWithPermission;
        }
    }

    public PersistPrefMain getPersistMain() {
        return persistMain;
    }

    public void setPersistMain(PersistPrefMain persist) {
        if (this.persistMain == null) {
            this.persistMain = persist;
        }
    }

    public SyncScheduler getSyncScheduler() {
        return syncScheduler;
    }

    public void setSyncScheduler(SyncScheduler syncScheduler) {
        if (this.syncScheduler != null) {
            this.syncScheduler = syncScheduler;
        }
    }

    public void resetForTest() {
        this.wallpaperScheduler = null;
        this.wallpaperSchedulerWithPermission = null;
        this.persistMain = null;
    }
}
