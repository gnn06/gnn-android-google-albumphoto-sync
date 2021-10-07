package gnn.com.photos.service;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

public class PhotosLocalService {

    /**
     * retrieve photo into given folder
     */
    public ArrayList<Photo> getLocalPhotos(File folder) {
        String[] list = folder.list();
        if (list != null) {
            ArrayList<Photo> result = new ArrayList<>(list.length);
            for (String filename : list) {
                String id = FilenameUtils.removeExtension(filename);
                result.add(new Photo(null, id));
            }
            return result;
        } else {
            return new ArrayList<>(0);
        }

    }

    /**
     * Delete given local files.
     */
    public void delete(ArrayList<Photo> toDelete, File folder, Synchronizer synchronizer) {
        // require Logger was initialized
        Logger logger = Logger.getLogger("worker");

        int delete = 0;
        for (Photo photo : toDelete) {
            File file = new File(folder, photo.getPhotoLocalFileName());
            if (!file.delete()) {
                logger.severe("error deleting");
            }
            delete++;
            synchronizer.incCurrentDelete();
        }
        logger.info("deleted count = " + delete);
    }
}
