package gnn.com.photos.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.stat.stat.WallpaperStatProvider;

/**
 * Choose a photo from local folder
 * persist the choice, update stat and notify observer
 * Use service to retrieve photo
 */
public class ChooseOneLocalPhotoPersist {

    private File photoFolder;
    private File processFolder;
    private PersistWallpaperTime persistChooseTime;
    private PersistChoose persistChoose;
    private WallpaperStatProvider statProvider;
    private WallpaperObserver observer;

    private static ChooseOneLocalPhotoPersist mInstance;
    private boolean isConfigured;

    static public ChooseOneLocalPhotoPersist getInstance() {
        if (mInstance == null) {
            mInstance = new ChooseOneLocalPhotoPersist();
        }
        return mInstance;
    }

    static public ChooseOneLocalPhotoPersist getInstance(File destinationFolder, File processFolder) {
        if (mInstance == null) {
            mInstance = new ChooseOneLocalPhotoPersist(destinationFolder, processFolder);
        }
        mInstance.config(destinationFolder, processFolder);
        return mInstance;
    }

    private ChooseOneLocalPhotoPersist() {}

    private ChooseOneLocalPhotoPersist(File photoFolder, File processFolder) {
        config(photoFolder, processFolder);
    }

    private void config(File photoFolder, File processFolder) {
        this.isConfigured = true;
        this.photoFolder = photoFolder;
        this.processFolder = processFolder;
        this.persistChooseTime = new PersistWallpaperTime(this.processFolder);
        this.persistChoose = new PersistChoose(this.processFolder);
        this.statProvider = new WallpaperStatProvider(this.processFolder);
    }

    public Photo chooseOne() {
        Logger logger = Logger.getLogger();
        Photo photo = chooseLocalPhoto(photoFolder);
        if (photo != null) {
            logger.info("PhotoChooser has choose " + photo.getId());
            try {
                persistChoose.write(photo);
                persistChooseTime.storeTime();
                // TODO transform observers into list
                statProvider.onWallpaperChange();
                if (observer != null) {
                    observer.onWallpaper();
                }
            } catch (IOException e) {
                Logger.getLogger().severe("can not write last wallpaper time");
            }
        }
        return photo;
    }

    public void addObserver(WallpaperObserver observer) {
        this.observer = observer;
    }

    public void removeObserver(WallpaperObserver observer) {
        this.observer = null;
    }

    /**
     *
     * @return null if no photo found
     * @param folder
     */
    private Photo chooseLocalPhoto(File folder) {
        PhotosLocalService pls = new PhotosLocalService();
        ArrayList<Photo> localPhotos = pls.getLocalPhotos(folder);
        Logger logger = Logger.getLogger();
        if (localPhotos.size() > 0) {
            ArrayList<Photo> photos = new PhotoChooserList().chooseOneList(localPhotos, 1, null);
            return photos.get(0);
        }
        // TODO manage no local photo
        return null;
    }

    public boolean isConfigured() {
        return isConfigured;
    }
}
