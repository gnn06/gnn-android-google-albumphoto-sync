package gnn.com.photos.service;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchGetMediaItemsResponse;
import com.google.photos.library.v1.proto.MediaItemResult;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.Synchronizer;

abstract class PhotoProvider {

    public PhotoProvider() {
    }

    /**
     * Retrieve remote album list
     */
    ArrayList<String> getAlbums() throws IOException, GoogleAuthException {
        ArrayList<String> albumNames = new ArrayList<>();
        InternalPhotosLibraryClient.ListAlbumsPagedResponse albums = getClient().listAlbums();
        for (Album album : albums.iterateAll()) {
            albumNames.add(album.getTitle());
        }
        return albumNames;
    }

    ArrayList<Photo> getPhotosFromAlbum(String albumName, Synchronizer synchronizer) throws IOException, GoogleAuthException {
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
        ArrayList<Photo> result = new ArrayList<>();
        for (int i = 0; i < ids.size(); i += 50) {
            List<String> slice = ids.subList(i, i + 50 >= ids.size() ? ids.size() : i + 50);
            BatchGetMediaItemsResponse response = getClient().batchGetMediaItems(slice);
            for (MediaItemResult item : response.getMediaItemResultsList()) {
                // code == 3 if id does not exist
                if (item.getStatus().getCode() == 0) {
                    result.add(new Photo(item.getMediaItem().getBaseUrl(), item.getMediaItem().getId()));
                }
            }
        }
        return result;
    }

    abstract PhotosLibraryClient getClient() throws IOException, GoogleAuthException;

}