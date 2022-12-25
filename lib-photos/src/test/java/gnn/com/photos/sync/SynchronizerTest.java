package gnn.com.photos.sync;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerTest {

    @Test
    public void observer() throws IOException, RemoteException {
        PhotosRemoteService remote = mock(PhotosRemoteService.class);
        PhotosLocalService locale = mock(PhotosLocalService.class);

        // Instantiate Synchronizer and observer
        Synchronizer synchronizer = new Synchronizer(remote, locale, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
        };
        SyncProgressObserver observer = mock(SyncProgressObserver.class);
        synchronizer.setObserver(observer);
        // when
        synchronizer.incCurrentDownload();
        synchronizer.incCurrentDelete();
        synchronizer.incAlbumSize();
        // then
        verify(observer).incCurrentDownload();
        verify(observer).incCurrentDelete();
        verify(observer).incAlbumSize();
    }

    @Test
    public void observer_inside_delayed() throws IOException, RemoteException {
        PhotosRemoteService remote = mock(PhotosRemoteService.class);
        PhotosLocalService locale = mock(PhotosLocalService.class);

        // Instantiate Synchronizer and observer
        Synchronizer synchronizer = new Synchronizer(remote, locale, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
        };
        SynchronizerDelayed synchronizerDelayed = new SynchronizerDelayed(12, synchronizer);

        SyncProgressObserver observer = mock(SyncProgressObserver.class);
        synchronizerDelayed.setObserver(observer);
        // when
        synchronizer.incCurrentDownload();
        synchronizer.incCurrentDelete();
        synchronizer.incAlbumSize();
        // then
        verify(observer).incCurrentDownload();
        verify(observer).incCurrentDelete();
        verify(observer).incAlbumSize();
    }
}