package gnn.com.photos.service;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.Synchronizer;

import static org.mockito.Mockito.*;

public class PhotosRemoteServiceTest {

    private Synchronizer synchronizer;
    private PhotoProvider photoProvider;

    @Before
    public void setUp() throws Exception {
        synchronizer = mock(Synchronizer.class);
        photoProvider = mock(PhotoProvider.class);
    }

    @Test
    public void testGetRemotePhotos_emptyCache() throws IOException, GoogleAuthException {
        // given, an empty cache
        PhotosRemoteService service = new PhotosRemoteService() {
            @Override
            public PhotoProvider getPhotoProvider() {
                return photoProvider;
            }
        };
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        when(photoProvider.getPhotos("album", synchronizer)).thenReturn(photos);

        // when call remotePhotos
        ArrayList<Photo> result = service.getPhotos("album", synchronizer);

        // then check that obtains an answer and put result into cache
        assertEquals(photos, result);
        assertEquals(photos, PhotosRemoteService.getCache().get());
    }

    @Test
    public void testGetRemotePhotos_notEmptyCache() throws IOException, GoogleAuthException {
        // given, an not empty cache
        PhotosRemoteService service = spy(new PhotosRemoteService() {
            @Override
            public PhotoProvider getPhotoProvider() {
                return null;
            }
        });
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        PhotosRemoteService.getCache().put(photos);

        // when call remotePhotos
        ArrayList<Photo> result = service.getPhotos("album", synchronizer);

        // then check that obtains an answer without calling the service
        assertEquals(photos, result);
        verify(photoProvider, times(0)).getPhotos(anyString(), (Synchronizer) anyObject());
    }
}