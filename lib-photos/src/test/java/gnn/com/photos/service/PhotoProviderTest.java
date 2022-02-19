package gnn.com.photos.service;

import static org.junit.Assert.assertEquals;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchGetMediaItemsResponse;
import com.google.photos.library.v1.proto.MediaItemResult;
import com.google.photos.types.proto.MediaItem;
import com.google.rpc.Status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gnn.com.photos.Photo;

@RunWith(MockitoJUnitRunner.class)
public class PhotoProviderTest {

    @Test
    public void more_than_50() throws IOException, RemoteException {
        // given
        ArrayList<String> ids = createIds(55);

        final PhotosLibraryClient client = Mockito.mock(PhotosLibraryClient.class);
        PhotoProvider provider = new PhotoProvider() {
            @Override
            protected PhotosLibraryClient getClient() throws RemoteException {
                return client;
            }
        };

        BatchGetMediaItemsResponse response = Mockito.mock(BatchGetMediaItemsResponse.class);

        Mockito.when(client.batchGetMediaItems(ArgumentMatchers.any(List.class))).thenReturn(response);

        ArrayList<MediaItemResult> photos = createPhotos(55);
        Mockito.when(response.getMediaItemResultsList())
                .thenReturn(photos.subList(0, 50))
                .thenReturn(photos.subList(50, 55));

        // when
        ArrayList<Photo> actual = provider.getPhotosFromIds(ids);

        // then
        Assert.assertEquals(55, actual.size());
        assertEquals("url1", actual.get(0).getUrl());
        assertEquals("url50", actual.get(50-1).getUrl());
        assertEquals("url55", actual.get(55-1).getUrl());
    }

    @Test
    public void less_than_50() throws IOException, RemoteException {
        // given
        ArrayList<String> ids = createIds(25);
        ArrayList<MediaItemResult> photos = createPhotos(25);

        final PhotosLibraryClient client = Mockito.mock(PhotosLibraryClient.class);
        PhotoProvider provider = new PhotoProvider() {
            @Override
            protected PhotosLibraryClient getClient() throws RemoteException {
                return client;
            }
        };

        BatchGetMediaItemsResponse response = Mockito.mock(BatchGetMediaItemsResponse.class);
        Mockito.when(client.batchGetMediaItems(ArgumentMatchers.any(List.class))).thenReturn(response);
        Mockito.when(response.getMediaItemResultsList())
                .thenReturn(photos);

        // when
        ArrayList<Photo> actual = provider.getPhotosFromIds(ids);

        // then
        Assert.assertEquals(25, actual.size());
        assertEquals("url1", actual.get(0).getUrl());
        assertEquals("url25", actual.get(25-1).getUrl());
    }

    private ArrayList<String> createIds(int size) {
        ArrayList<String> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String id = "id" + (i + 1);
            result.add(id);
        }
        return result;
    }

    private ArrayList<MediaItemResult> createPhotos(int size) {
        ArrayList<MediaItemResult> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String id = "id" + (i + 1);
            String url = "url" + (i + 1);
            MediaItemResult item = Mockito.mock(MediaItemResult.class);
            Mockito.when(item.getStatus()).thenReturn(Mockito.mock(Status.class));
            Mockito.when(item.getStatus().getCode()).thenReturn(0);
            MediaItem mediaItem = Mockito.mock(MediaItem.class);
            Mockito.when(mediaItem.getBaseUrl()).thenReturn(url);
            Mockito.when(mediaItem.getId()).thenReturn(id);
            Mockito.when(item.getMediaItem()).thenReturn(mediaItem);
            result.add(item);
        }
        return result;
    }
}
