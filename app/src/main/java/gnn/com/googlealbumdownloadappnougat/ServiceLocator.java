package gnn.com.googlealbumdownloadappnougat;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.WallpaperSchedulerWithPermission;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ServiceLocator {

    private static ServiceLocator _ServiceLocator;
    private WallpaperSchedulerWithPermission syncTask;

    public static ServiceLocator getInstance() {
        if (_ServiceLocator == null) {
            _ServiceLocator = new ServiceLocator();
        }
        return _ServiceLocator;
    }

    private WallpaperScheduler wallpaperScheduler;

    public WallpaperScheduler getWallpaperScheduler() {
        return wallpaperScheduler;
    }

    public void setWallpaperScheduler(WallpaperScheduler wallpaperScheduler) {
        if (this.wallpaperScheduler == null) {
            this.wallpaperScheduler = wallpaperScheduler;
        }
    }

    public void setSyncTask(WallpaperSchedulerWithPermission syncTask) {
        if (this.syncTask == null) {
            this.syncTask = syncTask;
        }
    }

    public WallpaperSchedulerWithPermission getSyncTask() {
        return syncTask;
    }
}
