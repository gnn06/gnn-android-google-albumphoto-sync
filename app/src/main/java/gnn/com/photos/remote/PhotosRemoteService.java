package gnn.com.photos.remote;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;

public abstract class PhotosRemoteService {

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

    /**
     * Retrieve remote photo list.
     */
    public ArrayList<Photo> getRemotePhotos(String albumName) throws IOException, GoogleAuthException {

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
                }
                return result;
            }
        }
        return null;
    }

    protected abstract PhotosLibraryClient getPhotoLibraryClient() throws IOException, GoogleAuthException;

}
