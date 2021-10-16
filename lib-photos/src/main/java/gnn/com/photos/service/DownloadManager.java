package gnn.com.photos.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

public class DownloadManager {

    /**
     * Download given photo into given folder.
     * Add .jpg to filename.
     * manage progress status bar
     */
    public void download(ArrayList<Photo> toDownload, File destinationFolder, String rename, Synchronizer synchronizer) throws IOException {
        // require Logger was initialized
        Logger logger = Logger.getLogger("worker");

        logger.fine("start download for " + toDownload.size() + " photo");
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
                File destination = photo.getPhotoLocalFile(destinationFolder);
                copy(source, destination);
                count++;
                synchronizer.incCurrentDownload();
            } catch (MalformedURLException e) {
                logger.severe("erreur " + photo + e.toString());
            }
        }
        logger.fine("downloaded count = " + count);
    }

    void copy(URL source, File destination) throws IOException {
        // copy overwrite existing file.
        FileUtils.copyURLToFile(source, destination);
    }
}
