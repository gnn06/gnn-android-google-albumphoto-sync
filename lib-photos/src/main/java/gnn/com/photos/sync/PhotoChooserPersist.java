package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.stat.stat.WallpaperStatProvider;

public class PhotoChooserPersist {

    private final File photoFolder;
    private final File processFolder;
    private final PersistWallpaperTime persist;
    private final WallpaperStatProvider statProvider;

    public PhotoChooserPersist(File photoFolder, File processFolder) {
        this.photoFolder = photoFolder;
        this.processFolder = processFolder;
        persist = new PersistWallpaperTime(this.processFolder);
        statProvider = new WallpaperStatProvider(this.processFolder);
    }

    public Photo chooseOne() {
        Logger logger = Logger.getLogger();
        Photo photo = chooseLocalPhoto(photoFolder);
        logger.info("PhotoChooser has choose " + photo.getId());
        try {
            persist.storeTime();
            statProvider.onWallpaperChange();
        } catch (IOException e) {
            Logger.getLogger().severe("can not write last wallpaper time");
        }
        return photo;
    }

    /**
     *
     * @return null if no photo found
     * @param folder
     */
    private Photo chooseLocalPhoto(File folder) {
        PhotosLocalService pls = new PhotosLocalService();
        ArrayList<Photo> localPhotos = pls.getLocalPhotos(folder);
        if (localPhotos.size() > 0) {
            Logger logger = Logger.getLogger();
            ArrayList<Photo> photos = new PhotoChooserList().chooseOneList(localPhotos, 1, null);
            return photos.get(0);
        }
        // TODO manage no local photo
        return null;
    }
}
