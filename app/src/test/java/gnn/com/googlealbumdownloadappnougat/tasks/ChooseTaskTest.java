package gnn.com.googlealbumdownloadappnougat.tasks;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterMain;
import gnn.com.photos.service.RemoteException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChooseTaskTest {

    @Test
    public void syncAll() throws IOException, RemoteException {
        SynchronizerAndroid synchroniser = mock(SynchronizerAndroid.class);
        IPresenterMain presenter = mock(PresenterMain.class);
        SyncTask chooseTask = new SyncTask(presenter, synchroniser);
        // given
        when(presenter.getQuantity()).thenReturn(-1);
        when(presenter.getAlbum()).thenReturn("album");
        when(presenter.getFolder()).thenReturn(mock(File.class));
        // when
        chooseTask.doInBackground(null);
        // then
        verify(synchroniser).syncAll(anyString(), any(File.class), (String)isNull());
    }

    @Test
    public void syncRandom() throws IOException, RemoteException {
        SynchronizerAndroid synchroniser = mock(SynchronizerAndroid.class);
        IPresenterMain presenter = mock(PresenterMain.class);
        SyncTask chooseTask = new SyncTask(presenter, synchroniser);
        // given
        when(presenter.getQuantity()).thenReturn(5);
        when(presenter.getAlbum()).thenReturn("album");
        when(presenter.getFolder()).thenReturn(mock(File.class));
        // when
        chooseTask.doInBackground(null);
        // then
        verify(synchroniser).syncRandom(anyString(), any(File.class), (String)isNull(), anyInt());
    }
}