package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.local.PhotosLocalService;
import gnn.com.photos.model.Photo;
import gnn.com.photos.remote.PhotosRemoteService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SynchronizerTest {

    ArrayList<Photo> remotePhotos = new ArrayList<>();
    ArrayList<Photo> localPhotos = new ArrayList<>();

    @Test
    public void chooseOne() throws IOException, GoogleAuthException {
        // given
        remotePhotos.add(new Photo("url1", "id1"));
        remotePhotos.add(new Photo("url3", "id3"));

        localPhotos.add(new Photo("url2", "id2"));

        final File folder = Mockito.mock(File.class);

        final PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);
        when(prs.getRemotePhotos(Mockito.anyString(), (Synchronizer) Mockito.anyObject())).thenReturn(remotePhotos);

        final PhotosLocalService pls = Mockito.mock(PhotosLocalService.class);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        Synchronizer sync = new Synchronizer() {
            @Override
            protected PhotosRemoteService getRemoteService() {
                return prs;
            }

            @Override
            public void incAlbumSize() {

            }

            @Override
            protected PhotosLocalService getLocalService() {
                return pls;
            }
        };

        // when
        sync.chooseOne("album", folder);

        // then
        // check download one
        ArgumentCaptor<ArrayList> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(prs).download(captor.capture(), (File) anyObject(), (Synchronizer) anyObject());
        assertEquals(1, captor.getValue().size());

        // check delete all
        verify(pls).delete(localPhotos, folder, sync);
    }
}