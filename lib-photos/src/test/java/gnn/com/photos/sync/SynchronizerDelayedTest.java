package gnn.com.photos.sync;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public class SynchronizerDelayedTest {

    private File folder;

    public SynchronizerDelayedTest() {
    }

    @Before
    public void setUp() throws Exception {
        Logger.configure();
        File folder = tempFolder.newFolder();
    }

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void syncRandom_no_previous_sync() throws IOException, RemoteException {
        // given
        Synchronizer sync = mock(Synchronizer.class);
        SynchronizerDelayed syncDelayed = new SynchronizerDelayed(12, sync);

        // when
        when(sync.retrieveLastSyncTime()).thenReturn(null);

        syncDelayed.syncRandom("album", tempFolder.newFolder(), null, -1);

        // then
        verify(sync, never()).syncRandom("album", folder, null, -1);
    }

    @Test
    public void syncRandom_not_expired() throws IOException, RemoteException {
        // given
        Synchronizer sync = mock(Synchronizer.class);
        SynchronizerDelayed syncDelayed = new SynchronizerDelayed(60, sync);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -30);
        Date date_before_expiration = calendar.getTime();
        when(sync.retrieveLastSyncTime()).thenReturn(date_before_expiration);

        // when
        syncDelayed.syncRandom("album", folder, null, -1);

        // then
        verify(sync, never()).syncRandom("album", folder, null, -1);
    }

    @Test
    public void syncRandom_expired() throws IOException, RemoteException {
        // given
        Synchronizer sync = mock(Synchronizer.class);
        SynchronizerDelayed syncDelayed = new SynchronizerDelayed(60, sync);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -70);
        Date date_before_expiration = calendar.getTime();
        when(sync.retrieveLastSyncTime()).thenReturn(date_before_expiration);

        // when
        syncDelayed.syncRandom("album", folder, null, -1);

        // then
        verify(sync, atLeastOnce()).syncRandom("album", folder, null, -1);
    }

    @Test
    public void syncRandom_disable() throws IOException, RemoteException {
        // given
        Synchronizer sync = mock(Synchronizer.class);
        SynchronizerDelayed syncDelayed = new SynchronizerDelayed(SynchronizerDelayed.DELAY_NEVER_SYNC, sync);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -70);
        Date date_before_expiration = calendar.getTime();
        when(sync.retrieveLastSyncTime()).thenReturn(date_before_expiration);

        // when
        syncDelayed.syncRandom("album", folder, null, -1);

        // then
        verify(sync, never()).syncRandom("album", folder, null, -1);
    }

    @Test
    public void syncRandom_always_sync() throws IOException, RemoteException {
        // based on syncRandom_not_expired
        // given
        Synchronizer sync = mock(Synchronizer.class);
        SynchronizerDelayed syncDelayed = new SynchronizerDelayed(SynchronizerDelayed.DELAY_ALWAYS_SYNC, sync);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MINUTE, -30);
        Date date_before_expiration = calendar.getTime();
        when(sync.retrieveLastSyncTime()).thenReturn(date_before_expiration);

        // when
        syncDelayed.syncRandom("album", folder, null, -1);

        // then
        verify(sync, atLeastOnce()).syncRandom("album", folder, null, -1);
    }
}