package gnn.com.photos.sync;

import java.io.File;

public class PersistWallpaperTime extends PersistTime {
    public PersistWallpaperTime(File processFolder) {
        super(processFolder, "last_wallpaper");
    }
}
