package gnn.com.photos.service;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.util.ArrayMap;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchGetMediaItemsResponse;
import com.google.photos.library.v1.proto.MediaItemResult;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.Synchronizer;

public abstract class PhotoProvider {

    private static final String TAG = "PhotoProvider";

    protected PhotosLibraryClient client;

    public PhotoProvider() {
    }

    /**
     * Retrieve remote album list
     */
    public ArrayList<String> getAlbums() throws IOException, GoogleAuthException {
        ArrayList<String> albumNames = new ArrayList<>();
        InternalPhotosLibraryClient.ListAlbumsPagedResponse albums = getClient().listAlbums();
        for (Album album : albums.iterateAll()) {
            albumNames.add(album.getTitle());
        }
        return albumNames;
    }

    public ArrayList<Photo> getPhotosFromAlbum(String albumName, Synchronizer synchronizer) throws IOException, GoogleAuthException {
        InternalPhotosLibraryClient.ListAlbumsPagedResponse response = getClient().listAlbums();
        for (Album album : response.iterateAll()) {
            String title = album.getTitle();
            if (albumName.equals(title)) {
                String albumId = album.getId();
                //System.out.println("albumId=" + albumId);
                InternalPhotosLibraryClient.SearchMediaItemsPagedResponse responsePhotos = getClient().searchMediaItems(albumId);
                ArrayList<Photo> result = new ArrayList<>();
                for (MediaItem item : responsePhotos.iterateAll()) {
                    result.add(new Photo(item.getBaseUrl(), item.getId()));
                    synchronizer.incAlbumSize();
                }
                return result;
            }
        }
        return null;
    }

    ArrayList<Photo> getPhotosFromIds(ArrayList<String> ids) throws IOException, GoogleAuthException {
        ArrayList<Photo> result = null;
        BatchGetMediaItemsResponse response = client.batchGetMediaItems(ids);
        for (MediaItemResult item : response.getMediaItemResultsList()) {
            // code == 3 if id does not exist
            if (item.getStatus().getCode() == 0) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(new Photo(item.getMediaItem().getBaseUrl(), item.getMediaItem().getId()));
            }
        }
        return result;
    }

    public PhotosLibraryClient getClient() throws IOException, GoogleAuthException {
        if (client != null) {
            Log.d(TAG, "get photo library client from cache");
            return client;
        } else {
            return getClientImpl();
        }
    }

    abstract PhotosLibraryClient getClientImpl() throws IOException, GoogleAuthException;

}