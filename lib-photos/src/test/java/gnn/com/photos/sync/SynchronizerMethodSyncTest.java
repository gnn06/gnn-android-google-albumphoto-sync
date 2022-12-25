package gnn.com.photos.sync;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.Arrays;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;

public class SynchronizerMethodSyncTest {

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
        Logger.configure();
        remotePhotos = new ArrayList<>();
        localPhotos = new ArrayList<>();

        remotePhotos.add(new Photo("url1", "id1"));
        remotePhotos.add(new Photo("url3", "id3"));

        localPhotos.add(new Photo("url2", "id2"));

        prs = mock(PhotosRemoteService.class);
        pls = mock(PhotosLocalService.class);


        SyncProgressObserver observer = mock(SyncProgressObserver.class);
        synchronizer = spy(new Synchronizer(prs, pls, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return prs;
            }

            @Override
            public void incAlbumSize() {}
        });
        synchronizer.setObserver(observer);

        folder = mock(File.class);

        when(prs.getPhotos("album", observer)).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

    }

    @Test
    public void sync_all() throws IOException, RemoteException {
        // given remote photos that don't be local and local photo was dont't be remote
        when(prs.getPhotos(anyString(), ArgumentMatchers.any())).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        // when calling sync
        synchronizer.sync("album", folder, null, -1);

        // then, check download all
        verify(prs).download(eq(remotePhotos), eq(folder), eq(null), any());

        // and check delete all
        verify(pls).delete(eq(localPhotos), eq(folder), any());
    }

    @Test
    public void sync_observer() throws IOException, RemoteException {
        // given remote photos that don't be local and local photo was dont't be remote
        when(prs.getPhotos(anyString(), ArgumentMatchers.any())).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        // when calling sync
        synchronizer.sync("album", folder, null, -1);

        // then, check download all
        verify(synchronizer).begin();
        verify(synchronizer).end();
    }

    @Test
    public void sync_chooseOne() throws IOException, RemoteException {
        // given remote photos that don't be local and local photo that don't be remote
        when(prs.getPhotos(anyString(), ArgumentMatchers.any())).thenReturn(remotePhotos);
        when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

        synchronizer.sync("album", folder, null, 1);

        // then, check download was called with a oneList collection
        ArgumentCaptor<ArrayList> captor = ArgumentCaptor.forClass(ArrayList.class);
        verify(prs).download(captor.capture(), ArgumentMatchers.any(File.class), (String) ArgumentMatchers.isNull(), ArgumentMatchers.any());
        Assert.assertEquals(1, captor.getValue().size());

        // and check delete all
        verify(pls).delete(eq(localPhotos), eq(folder), any());
    }

    @Test
    public void throw_into_sync() throws IOException, RemoteException {
        //given

        doThrow(new IOException()).when(prs).download(ArgumentMatchers.any(ArrayList.class), ArgumentMatchers.any(File.class), (String) ArgumentMatchers.isNull(), ArgumentMatchers.any());

        // when
        try {
            synchronizer.sync("album", folder, null, 1);
        } catch (IOException ignored) {}

        // then assert that delete was not called
        verify(pls, never()).delete(ArgumentMatchers.any(ArrayList.class), ArgumentMatchers.any(File.class), ArgumentMatchers.any());
    }

    @Test
    public void test_write_last_sync_time() throws IOException, RemoteException {
        synchronizer.storeSyncTime();
    }

    @Test
    public void test_read_lastSyncTime() throws IOException {
        // given a synchronizer with a processFolder containing a last_sync file
        File tempFile = tempFolder.newFile("last_sync");

        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, tempFolder.getRoot(), null) {
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
        Synchronizer synchronizer = new Synchronizer(null, 24 * 60 * 60 * 1000, tempFolder.getRoot(), null) {
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