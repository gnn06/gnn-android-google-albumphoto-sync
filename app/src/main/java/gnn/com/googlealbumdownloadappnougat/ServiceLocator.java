package gnn.com.googlealbumdownloadappnougat;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.WallpaperSchedulerWithPermission;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ServiceLocator {

    private static ServiceLocator _ServiceLocator;
    private WallpaperSchedulerWithPermission syncTask;
    private WallpaperScheduler wallpaperScheduler;
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

    public WallpaperSchedulerWithPermission getSyncTask() {
        return syncTask;
    }

    public void setSyncTask(WallpaperSchedulerWithPermission syncTask) {
        if (this.syncTask == null) {
            this.syncTask = syncTask;
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

    public void resetForTest() {
        this.wallpaperScheduler = null;
        this.syncTask = null;
        this.persistMain = null;
    }
}
