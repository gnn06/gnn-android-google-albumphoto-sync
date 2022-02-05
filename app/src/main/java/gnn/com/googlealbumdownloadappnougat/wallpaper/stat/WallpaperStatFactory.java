package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

class WallpaperStatFactory {

    private final DateProvider dateProvider;

    WallpaperStatFactory(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    WallpaperStat get() {
        Date currentDate = dateProvider.get();
        return new WallpaperStat(currentDate);
    }
}
