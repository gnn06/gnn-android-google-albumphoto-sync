package gnn.com.photos.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

public class PhotosRemoteServiceTest {

    private Synchronizer synchronizer;
    private PhotoProvider photoProvider;
    private ArrayList<Photo> photos;
    private SyncProgressObserver observer;

    @Before
    public void setUp() throws Exception {
        synchronizer = mock(Synchronizer.class);
        photoProvider = mock(PhotoProvider.class);
        photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));

        Cache.config(null, -1);
        Cache.getCache().reset();
        this.observer = mock(SyncProgressObserver.class);
    }

    @Test
    public void testGetRemotePhotos_emptyCache() throws IOException, RemoteException {
        // given, an empty cache
        PhotosRemoteService service = new PhotosRemoteService() {
            @Override
            public PhotoProvider getPhotoProvider() {
                return photoProvider;
            }
        };
        Mockito.when(photoProvider.getPhotosFromAlbum("album", observer)).thenReturn(photos);
        Cache.getCache().reset();

        // when call remotePhotos
        ArrayList<Photo> result = service.getPhotos("album", observer);

        // then check that obtains an answer and put result into cache
        Assert.assertEquals(photos, result);
        assertEquals(photos, Cache.getCache().get());
    }

    @Test
    public void testGetRemotePhotos_notEmptyCache() throws IOException, RemoteException {
        // given, an not empty cache
        PhotosRemoteService service = Mockito.spy(new PhotosRemoteService() {
            @Override
            public PhotoProvider getPhotoProvider() {
                return null;
            }
        });
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        Cache.getCache().put(photos);

        // when call remotePhotos
        ArrayList<Photo> result = service.getPhotos("album", observer);

        // then check that obtains an answer without calling the service
        Assert.assertEquals(photos, result);
        Mockito.verify(photoProvider, Mockito.times(0)).getPhotosFromAlbum(ArgumentMatchers.anyString(), any());
    }

    @Test
    public void refreshBaseUrl() throws IOException, RemoteException {
        PhotosRemoteService service = new PhotosRemoteService() {
            @Override
            public PhotoProvider getPhotoProvider() {
                return photoProvider;
            }
        };

        ArrayList<String> ids = new ArrayList<>(Arrays.asList("id1"));

        ArrayList<Photo> expected = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1")
        ));

        Mockito.when(photoProvider.getPhotosFromIds(ids)).thenReturn(expected);

        // when call refreshBaeUrl with a photo which has non url
        ArrayList<Photo> photosWithNull = new ArrayList<>(Arrays.asList(
                new Photo(null, "id1")));
        ArrayList<Photo> actual = service.refreshBaseUrl(photosWithNull);

        // then, url was setted
        assertEquals("url1", actual.get(0).getUrl());
    }
}