package gnn.com.photos.service;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchGetMediaItemsResponse;
import com.google.photos.library.v1.proto.MediaItemResult;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import java.util.ArrayList;
import java.util.List;

import gnn.com.photos.Photo;

public abstract class PhotoProvider {

    public PhotoProvider() {
    }

    /**
     * Retrieve remote album list
     */
    ArrayList<String> getAlbums() throws RemoteException {
        ArrayList<String> albumNames = new ArrayList<>();
        InternalPhotosLibraryClient.ListAlbumsPagedResponse albums = getClient().listAlbums();
        for (Album album : albums.iterateAll()) {
            albumNames.add(album.getTitle());
        }
        return albumNames;
    }

    ArrayList<Photo> getPhotosFromAlbum(String albumName, SyncProgressObserver synchronizer) throws RemoteException {
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

    ArrayList<Photo> getPhotosFromIds(List<String> ids) throws RemoteException {
        ArrayList<Photo> result = new ArrayList<>();
        if (ids.size() == 0) {
          return result;
        } else if (ids.size() > 50) {
            result.addAll(getPhotosFromIds(ids.subList(0, 50)));
            result.addAll(getPhotosFromIds(ids.subList(50, ids.size())));
        } else {
            BatchGetMediaItemsResponse response = getClient().batchGetMediaItems(ids);
            for (MediaItemResult item : response.getMediaItemResultsList()) {
                // code == 3 if id does not exist
                if (item.getStatus().getCode() == 0) {
                    result.add(new Photo(item.getMediaItem().getBaseUrl(), item.getMediaItem().getId()));
                }
            }
        }
        return result;
    }

    protected abstract PhotosLibraryClient getClient() throws RemoteException;

}