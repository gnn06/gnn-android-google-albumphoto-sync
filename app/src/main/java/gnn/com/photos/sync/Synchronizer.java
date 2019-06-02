package gnn.com.photos.sync;

import com.google.photos.library.v1.PhotosLibraryClient;

import org.apache.commons.lang3.builder.Diff;

import gnn.com.photos.local.PhotosLocalService;
import gnn.com.photos.remote.PhotosRemoteService;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class Synchronizer {

    // TODO: 07/05/2019 managed updated photo if possible
    public DiffCalculator sync(String albumName, File folder, PhotosLibraryClient client) {
        DiffCalculator sync = null;
        try {
            PhotosRemoteService prs = new PhotosRemoteService(client);
            PhotosLocalService pls = new PhotosLocalService();
            System.out.println("get photos of album : " + albumName);
            System.out.println("download photos into folder : " + folder);
            ArrayList remote = prs.getRemotePhotos(albumName);
            ArrayList local = pls.getLocalPhotos(folder);
            sync = new DiffCalculator(remote, local);
            pls.delete(sync.getToDelete(), folder);
            DownloadManager.download(sync.getToDownload(), folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sync;
    }
}
