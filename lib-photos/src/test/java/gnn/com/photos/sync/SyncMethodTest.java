package gnn.com.photos.sync;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public class SyncMethodTest {

    ArrayList<Photo> remotePhotos;
    ArrayList<Photo> localPhotos;
    PhotosRemoteService prs;
    PhotosLocalService pls;
    Synchronizer synchronizer;
    File folder;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException, RemoteException {
        remotePhotos = new ArrayList<>();
        localPhotos = new ArrayList<>();

        remotePhotos.add(new Photo("url1", "id1"));
        remotePhotos.add(new Photo("url3", "id3"));

        localPhotos.add(new Photo("url2", "id2"));

        prs = mock(PhotosRemoteService.class);
        pls = mock(PhotosLocalService.class);


        synchronizer = new Synchronizer(null, 0, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return prs;
            }

            @Override
            public void incAlbumSize() {}
        };

        folder = mock(File.class);

        when(prs.getPhotos("album", synchronizer)).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

    }

    @Test
    public void sync_all() throws IOException, RemoteException {
        SyncMethod syncMethod = new SyncMethod(synchronizer,prs,pls);
        // given remote photos that don't be local and local photo was dont't be remote
        when(prs.getPhotos(anyString(), ArgumentMatchers.any(Synchronizer.class))).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        // when calling sync
        syncMethod.sync("album", folder, null, -1);

        // then, check download all
        verify(prs).download(remotePhotos, folder, null, synchronizer);

        // and check delete all
        verify(pls).delete(localPhotos, folder, synchronizer);
    }

    @Test
    public void sync_observer() throws IOException, RemoteException {
        Synchronizer synchronizer = mock(Synchronizer.class);
        SyncMethod syncMethod = new SyncMethod(synchronizer,prs,pls);
        // given remote photos that don't be local and local photo was dont't be remote
        when(prs.getPhotos(anyString(), ArgumentMatchers.any(Synchronizer.class))).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        // when calling sync
        syncMethod.sync("album", folder, null, -1);

        // then, check download all
        verify(synchronizer).begin();
        verify(synchronizer).end();
    }

    @Test
    public void sync_chooseOne() throws IOException, RemoteException {
        // given remote photos that don't be local and local photo that don't be remote
        when(prs.getPhotos(anyString(), ArgumentMatchers.any(Synchronizer.class))).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        SyncMethod syncMethod = new SyncMethod(synchronizer, prs, pls) {};

        // when
        syncMethod.sync("album", folder, null, 1);

        // then, check download was called with a oneList collection
        ArgumentCaptor<ArrayList> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(prs).download(captor.capture(), ArgumentMatchers.any(File.class), (String) ArgumentMatchers.isNull(), ArgumentMatchers.any(Synchronizer.class));
        Assert.assertEquals(1, captor.getValue().size());

        // and check delete all
        verify(pls).delete(localPhotos, folder, synchronizer);
    }

    @Test
    public void throw_into_sync() throws IOException, RemoteException {
        //given
        SyncMethod syncMethod = new SyncMethod(synchronizer, prs, pls) {};

        doThrow(new IOException()).when(prs).download(ArgumentMatchers.any(ArrayList.class), ArgumentMatchers.any(File.class), (String) ArgumentMatchers.isNull(), ArgumentMatchers.any(Synchronizer.class));

        // when
        try {
            syncMethod.sync("album", folder, null, 1);
        } catch (IOException ignored) {}

        // then assert that delete was not called
        verify(pls, never()).delete(ArgumentMatchers.any(ArrayList.class), ArgumentMatchers.any(File.class), ArgumentMatchers.any(Synchronizer.class));
    }

    @Test
    public void test_write_last_sync_time() throws IOException, RemoteException {
        SyncMethod syncFull = new SyncMethod(mock(Synchronizer.class), mock(PhotosRemoteService.class), mock(PhotosLocalService.class));
        SyncMethod syncMethod = spy(syncFull);
        syncMethod.sync("album", mock(File.class), null, 0);
    }

    @Test
    public void test_read_lastSyncTime() throws IOException {
        // given a synchronizer with a processFolder containing a last_sync file
        File tempFile = tempFolder.newFile("last_sync");

        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, tempFolder.getRoot()) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
            @Override
            public void incAlbumSize() {
            }
        };

        // when call method
        Date lastSyncTimeActual = synchronizer.retrieveLastSyncTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String  stringLastSyncTimeActual = simpleDateFormat.format(lastSyncTimeActual);

        // then assert we gave the DateTime of the given file
        DateFormat sdf =  SimpleDateFormat.getInstance();
        String stringLastModifiedExpected = sdf.format(tempFile.lastModified());
        Assert.assertEquals(stringLastModifiedExpected, stringLastSyncTimeActual);
    }

    @Test
    public void test_readLastSyncTime_null() {
        // given
        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, tempFolder.getRoot()) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
            @Override
            public void incAlbumSize() {
            }
        };

        // when
        Date stringLastSyncTimeActual = synchronizer.retrieveLastSyncTime();

        // then
        Assert.assertNull(stringLastSyncTimeActual);
    }
}