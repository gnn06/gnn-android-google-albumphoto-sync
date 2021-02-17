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
    public void download(ArrayList<Photo> toDownload, File destinationFolder, String rename, Synchronizer synchronizer) throws IOException {
        // rename Id before download
        if (rename != null) {
            Photo.renameList(toDownload, rename);
        }
        int count = 0;
        for (Photo photo : toDownload) {
            URL source;
            try {
                // TODO: 06/05/2019 manage downloading photo resolution
                source = new URL(photo.getUrl());
                String destinationFileName = photo.getId() + getFileExtension();
                File destination = new File(destinationFolder, destinationFileName);
                copy(source, destination);
                count++;
                synchronizer.incCurrentDownload();
            } catch (MalformedURLException e) {
                // TODO: 06/05/2019 log instead of stderr
                System.err.println("erreur " + photo + e.toString());
            }
        }
        System.out.println("downloaded count = " + count);
    }

    void copy(URL source, File destination) throws IOException {
        // copy overwrite existing file.
        FileUtils.copyURLToFile(source, destination);
    }

    public static String getFileExtension() {
        // TODO: 06/05/2019 manage file extension from mimeType
        return ".jpg";
    }
}
