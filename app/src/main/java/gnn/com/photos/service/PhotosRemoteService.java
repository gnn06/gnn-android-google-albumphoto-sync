package gnn.com.photos.service;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.DownloadManager;
import gnn.com.photos.sync.Synchronizer;

public abstract class PhotosRemoteService {

    private static final String TAG = "PhotosRemoteService";

    /**
     * As class var, cache is common between PhotosRemoteService instances.
     */
    private static Cache cache;

    // Used to Unit test
    static Cache getCache() {
        return cache;
    }

    public PhotosRemoteService() {
        cache = new Cache(null, -1);
    }

    public PhotosRemoteService(File cacheFile, long cacheMaxAge) {
        cache = new Cache(cacheFile, cacheMaxAge);
    }

    protected PhotosLibraryClient client;

    /**
     * Retrieve remote album list
     */
    public ArrayList<String> getAlbums() throws IOException, GoogleAuthException {
        ArrayList<String> albumNames = new ArrayList<>();
        InternalPhotosLibraryClient.ListAlbumsPagedResponse albums = getPhotoLibraryClient().listAlbums();
        for (Album album : albums.iterateAll()) {
            albumNames.add(album.getTitle());
        }
        return albumNames;
    }

    public ArrayList<Photo> getPhotos(String album, Synchronizer synchronizer) throws IOException, GoogleAuthException {
        ArrayList<Photo> photos = cache.get();
        if (photos == null) {
            photos = getPhotosInternal(album, synchronizer);
            cache.put(photos);
        } else {
            Log.i(TAG, "use cache");
        }
        synchronizer.setAlbumSize(photos.size());
        return photos;
    }

    /**
     * Retrieve remote photo list.
     */
    ArrayList<Photo> getPhotosInternal(String albumName, Synchronizer sync) throws IOException, GoogleAuthException {

        InternalPhotosLibraryClient.ListAlbumsPagedResponse response = getPhotoLibraryClient().listAlbums();
        for (Album album : response.iterateAll()) {
            String title = album.getTitle();
            if (albumName.equals(title)) {
                String albumId = album.getId();
                //System.out.println("albumId=" + albumId);
                InternalPhotosLibraryClient.SearchMediaItemsPagedResponse responsePhotos = getPhotoLibraryClient().searchMediaItems(albumId);
                ArrayList<Photo> result = new ArrayList<>();
                for (MediaItem item : responsePhotos.iterateAll()) {
                    result.add(new Photo(item.getBaseUrl(), item.getId()));
                    sync.incAlbumSize();
                }
                return result;
            }
        }
        return null;
    }

    public void download(ArrayList<Photo> list, File folder, Synchronizer sync) throws IOException {
        new DownloadManager().download(list, folder, sync);
    }

    protected abstract PhotosLibraryClient getPhotoLibraryClient() throws IOException, GoogleAuthException;

    public void resetCache() {
        if (cache != null) {
            cache.reset();
        }
    }
}
