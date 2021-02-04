package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyncMethodTest {

    ArrayList<Photo> remotePhotos = new ArrayList<>();
    ArrayList<Photo> localPhotos = new ArrayList<>();

    @Test
    public void sync() throws IOException, GoogleAuthException {
        Synchronizer sync = mock(Synchronizer.class);
        SyncMethod syncMethod = new SyncFull(sync,
                mock(PhotosRemoteService.class),
                mock(PhotosLocalService.class));
        File folder = mock(File.class);
        File processFolder = mock(File.class);
        syncMethod.sync("album", folder, 0);
    }

    @Test
    public void chooseOne() throws IOException, GoogleAuthException {
        // given
        remotePhotos.add(new Photo("url1", "id1"));
        remotePhotos.add(new Photo("url3", "id3"));

        localPhotos.add(new Photo("url2", "id2"));

        final File folder = Mockito.mock(File.class);

        final PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);
        when(prs.getPhotos(Mockito.anyString(), (Synchronizer) Mockito.anyObject())).thenReturn(remotePhotos);

        final PhotosLocalService pls = Mockito.mock(PhotosLocalService.class);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return prs;
            }

            @Override
            public void incAlbumSize() {

            }
        };
        SyncMethod syncMethod = new SyncRandom(synchronizer, prs, pls);

        // when
        syncMethod.sync("album", folder, 1);

        // then
        // check download one
        ArgumentCaptor<ArrayList> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(prs).download(captor.capture(), (File) anyObject(), (Synchronizer) anyObject());
        assertEquals(1, captor.getValue().size());

        // check delete all
        verify(pls).delete(localPhotos, folder, synchronizer);
    }

    @Test
    public void test_write_last_sync_time() throws IOException, GoogleAuthException {
        String temp_path = System.getProperty("java.io.tmpdir");
        File processFolder = new File(temp_path);
        SyncFull syncFull = new SyncFull(mock(Synchronizer.class), mock(PhotosRemoteService.class), mock(PhotosLocalService.class));
        SyncMethod syncMethod = spy(syncFull);
        syncMethod.sync("album", mock(File.class), 0);
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    @Test
    public void test_read_lastSyncTime() throws IOException {
        // given a synchronizer with a processFolder containing a last_sync file
        File tempFile = folder.newFile("last_sync");

        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, folder.getRoot()) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
            @Override
            public void incAlbumSize() {
            }
        };

        // when call method
        String stringLastSyncTimeActual = synchronizer.retrieveLastSyncTime();

        // then assert we gave the DateTime of the given file
        DateFormat sdf =  SimpleDateFormat.getInstance();
        String stringLastModifiedExpected = sdf.format(tempFile.lastModified());
        assertEquals(stringLastModifiedExpected, stringLastSyncTimeActual);
    }

    @Test
    public void test_readLastSyncTime_null() {
        // given
        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, folder.getRoot()) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
            @Override
            public void incAlbumSize() {
            }
        };

        // when
        String stringLastSyncTimeActual = synchronizer.retrieveLastSyncTime();

        // then
        assertNull(stringLastSyncTimeActual);
    }

}