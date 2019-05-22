package gnn.com.photos.remote;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import gnn.com.photos.model.Photo;

import java.util.ArrayList;

public class PhotosRemoteService {

    private PhotosLibraryClient client;

    public PhotosRemoteService(PhotosLibraryClient client) {
        this.client = client;
    }

    public ArrayList getRemotePhotos(String albumName) {

        InternalPhotosLibraryClient.ListAlbumsPagedResponse response = client.listAlbums();
        for (Album album : response.iterateAll()) {
            String title = album.getTitle();
            if (albumName.equals(title)) {
                String albumId = album.getId();
                //System.out.println("albumId=" + albumId);
                InternalPhotosLibraryClient.SearchMediaItemsPagedResponse responsePhotos = client.searchMediaItems(albumId);
                int count = 0;
                ArrayList result = new ArrayList();
                for (MediaItem item : responsePhotos.iterateAll()) {
                    String filename = item.getFilename();
                    result.add(new Photo(item.getBaseUrl(), item.getId()));
                    count++;
                }
                return result;
            }
        }
        return null;
    }

}
