package gnn.com.photos.remote;

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
    protected PhotosLibraryClient client;

    public PhotosRemoteService() {
        cache = new Cache(null);
    }

    // Used to Unit test
    static Cache getCache() {
        return cache;
    }

    /**
     * As class var, cache is common between PhotosRemoteService instances.
     */
    private static Cache cache;

    public PhotosRemoteService(File cacheFile) {
        cache = new Cache(cacheFile);
    }

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

    public ArrayList<Photo> getRemotePhotos(String album, Synchronizer synchronizer) throws IOException, GoogleAuthException {
        ArrayList<Photo> photos = cache.get();
        if (photos == null) {
            photos = getRemotePhotosInternal(album, synchronizer);
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
    ArrayList<Photo> getRemotePhotosInternal(String albumName, Synchronizer sync) throws IOException, GoogleAuthException {

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
        DownloadManager.download(list, folder, sync);
    }

    protected abstract PhotosLibraryClient getPhotoLibraryClient() throws IOException, GoogleAuthException;
}
