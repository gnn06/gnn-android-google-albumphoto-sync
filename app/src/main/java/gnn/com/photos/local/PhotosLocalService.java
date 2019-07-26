package gnn.com.photos.local;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.DownloadManager;
import gnn.com.photos.sync.Synchronizer;

public class PhotosLocalService {

    /**
     *
     * @param folder
     * @return ArrayList(0) if folder is empty
     */
    public ArrayList<Photo> getLocalPhotos(File folder) {
        String[] list = folder.list();
        if (list != null) {
            ArrayList result = new ArrayList(list.length);
            for (String filename : list) {
                String id = FilenameUtils.removeExtension(filename);
                result.add(new Photo(null, id));
            }
            return result;
        } else {
            return new ArrayList(0);
        }

    }

    public void delete(ArrayList<Photo> toDelete, File folder, Synchronizer synchronizer) {
        int delete = 0;
        for (Photo photo : toDelete) {
            File file = new File(folder, photo.getId() + DownloadManager.getFileExtension());
            file.delete();
            delete++;
            synchronizer.incCurrentDelete();
        }
        System.out.println("deleted count = " + delete);
    }
}
