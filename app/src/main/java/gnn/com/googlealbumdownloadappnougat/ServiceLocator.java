package gnn.com.googlealbumdownloadappnougat;

import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class ServiceLocator {

    private static ServiceLocator _ServiceLocator;

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
        this.wallpaperScheduler = wallpaperScheduler;
    }
}
