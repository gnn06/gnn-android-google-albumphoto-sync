package gnn.com.photos.sync;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.model.Photo;

public class DownloadManager {

    public static void download(ArrayList<Photo> toDownload, File folder, Presenter.SyncTask syncTask) throws IOException {
        int count = 0;
        for (Photo photo : toDownload) {
            URL source;
            try {
                // TODO: 06/05/2019 manage downloading photo resolution
                source = new URL(photo.getUrl());
                File destination = new File(folder, photo.getId() + getFileExtension());
                FileUtils.copyURLToFile(source, destination);
                count++;
                syncTask.incrementCurrentDownloaded();
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
