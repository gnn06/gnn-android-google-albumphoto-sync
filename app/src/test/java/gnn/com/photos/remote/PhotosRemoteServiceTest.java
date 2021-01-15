package gnn.com.photos.remote;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.model.Photo;

import static org.junit.Assert.*;

public class PhotosRemoteServiceTest {

    @Test
    public void testGetRemotePhotos_emptyCache() throws IOException, GoogleAuthException {
        // given, an empty cache
        PhotosRemoteService service = Mockito.mock(PhotosRemoteService.class);
        Mockito.when(service.getRemotePhotos("album")).thenCallRealMethod();
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        Mockito.when(service.getRemotePhotosInternal("album")).thenReturn(photos);

        // when call remotePhotos
        ArrayList<Photo> result = service.getRemotePhotos("album");

        // then check that obtains an answer and put result into cache
        assertEquals(photos, result);
        assertEquals(photos, PhotosRemoteService.getCache().get());
    }

    @Test
    public void testGetRemotePhotos_notEmptyCache() throws IOException, GoogleAuthException {
        // given, an not empty cache
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        Cache cache = new Cache();
        cache.put(photos);
        PhotosRemoteService service = Mockito.mock(PhotosRemoteService.class);
        Mockito.when(service.getRemotePhotos("album")).thenCallRealMethod();

        // when call remotePhotos
        ArrayList<Photo> result = service.getRemotePhotos("album");

        // then check that obtains an answer without calling the service
        assertNotNull(result);
        Mockito.verify(service, Mockito.times(0)).getRemotePhotosInternal("album");
    }
}