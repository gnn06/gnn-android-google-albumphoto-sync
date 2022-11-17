package gnn.com.googlealbumdownloadappnougat.tasks;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerTask;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.photos.service.RemoteException;

public class SyncTaskTest {

    private PersistPrefMain preferences;

    @Before
    public void setUp() throws Exception {
        preferences = mock(PersistPrefMain.class);
    }

    @Test
    public void doInBackground_Error() throws IOException, RemoteException {
        IPresenterHome presenter = Mockito.mock(PresenterHome.class);
        SynchronizerTask sync = Mockito.mock(SynchronizerTask.class);
        PersistPrefMain preferences = mock(PersistPrefMain.class);
        File file = Mockito.mock(File.class);

        when(presenter.getAlbum()).thenReturn("album");
        when(((PresenterHome) presenter).getFolder()).thenReturn(file);
        when(preferences.getQuantity()).thenReturn(-1);
        doThrow(new RemoteException(null)).when(sync).syncAll("album", file, null);

        Context context= mock(Context.class);
        SyncTask task = Mockito.spy(new SyncTask(presenter, sync, preferences, context));

        task.doInBackground();

        verify(task).markAsError("gnn.com.photos.service.RemoteException");
    }

}
