package gnn.com.photos.sync;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;

public class DownloadManager {

    /**
     * Download given photo into given folder.
     * Add .jpg to filename.
     * manage progress status bar
     */
    public static void download(ArrayList<Photo> toDownload, File folder, Synchronizer synchronizer) throws IOException {
        int count = 0;
        for (Photo photo : toDownload) {
            URL source;
            try {
                // TODO: 06/05/2019 manage downloading photo resolution
                source = new URL(photo.getUrl());
                File destination = new File(folder, photo.getId() + getFileExtension());
                FileUtils.copyURLToFile(source, destination);
                count++;
                synchronizer.incCurrentDownload();
            } catch (MalformedURLException e) {
                // TODO: 06/05/2019 log instead of stderr
                System.err.println("erreur " + photo + e.getMessage());
            }
        }
        System.out.println("downloaded count = " + count);
    }

    public static String getFileExtension() {
        // TODO: 06/05/2019 manage file extension from mimeType
        return ".jpg";
    }
}
