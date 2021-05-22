package gnn.com.photos.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

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
                File destination = new File(destinationFolder, photo.getPhotoLocalFileName());
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
}
