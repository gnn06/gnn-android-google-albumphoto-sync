package gnn.com.photos.remote;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.photos.library.v1.PhotosLibraryClient;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.model.Photo;

import static org.junit.Assert.*;

public class PhotosRemoteServiceTest {

    @Test
    public void testGetRemotePhotos_emptyCache() throws IOException, GoogleAuthException {
        // given, an empty cache
        Cache cache = new Cache();
        PhotosRemoteService service = Mockito.mock(PhotosRemoteService.class);
        Mockito.when(service.getRemotePhotosWithCache("album", cache)).thenCallRealMethod();
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        Mockito.when(service.getRemotePhotos("album")).thenReturn(photos);

        // when call remotePhotos
        ArrayList<Photo> result = service.getRemotePhotosWithCache("album", cache);

        // then check that obtains an answer and put result into cache
        assertEquals(photos, result);
        assertEquals(photos, cache.get());
    }

    @Test
    public void testGetRemotePhotos_notEmptyCache() throws IOException, GoogleAuthException {
        // given, an not empty cache
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        Cache cache = new Cache();
        cache.put(photos);
        PhotosRemoteService service = Mockito.mock(PhotosRemoteService.class);
        Mockito.when(service.getRemotePhotosWithCache("album", cache)).thenCallRealMethod();

        // when call remotePhotos
        ArrayList<Photo> result = service.getRemotePhotosWithCache("album", cache);

        // then check that obtains an answer without calling the service
        assertNotNull(result);
        Mockito.verify(service, Mockito.times(0)).getRemotePhotos("album");
    }
}