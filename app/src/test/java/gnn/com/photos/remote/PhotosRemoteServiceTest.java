package gnn.com.photos.remote;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.Synchronizer;

import static org.junit.Assert.*;

public class PhotosRemoteServiceTest {

    @Test
    public void testGetRemotePhotos_emptyCache() throws IOException, GoogleAuthException {
        // given, an empty cache
        PhotosRemoteService service = Mockito.spy(PhotosRemoteService.class);
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        Mockito.doReturn(photos).when(service).getPhotosInternal(Mockito.anyString(), (Synchronizer) Mockito.anyObject());

        // when call remotePhotos
        ArrayList<Photo> result = service.getPhotos("album", Mockito.mock(Synchronizer.class));

        // then check that obtains an answer and put result into cache
        assertEquals(photos, result);
        assertEquals(photos, PhotosRemoteService.getCache().get());
    }

    @Test
    public void testGetRemotePhotos_notEmptyCache() throws IOException, GoogleAuthException {
        // given, an not empty cache
        PhotosRemoteService service = Mockito.spy(PhotosRemoteService.class);
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1")));
        PhotosRemoteService.getCache().put(photos);
        Mockito.doReturn(photos).when(service).getPhotosInternal(Mockito.anyString(), (Synchronizer) Mockito.anyObject());

        // when call remotePhotos
        ArrayList<Photo> result = service.getPhotos("album", Mockito.mock(Synchronizer.class));

        // then check that obtains an answer without calling the service
        assertEquals(photos, result);
        Mockito.verify(service, Mockito.times(0)).getPhotosInternal(Mockito.anyString(), (Synchronizer) Mockito.anyObject());
    }
}