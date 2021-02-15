package gnn.com.googlealbumdownloadappnougat.tasks;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterMain;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyncTaskTest {

    @Test
    public void doInBackground_Error() throws IOException, GoogleAuthException {
        IPresenterMain presenter = Mockito.mock(PresenterMain.class);
        SynchronizerAndroid sync = Mockito.mock(SynchronizerAndroid.class);
        File file = Mockito.mock(File.class);

        when(presenter.getAlbum()).thenReturn("album");
        when(((PresenterMain) presenter).getFolder()).thenReturn(file);
        when(presenter.getQuantity()).thenReturn(-1);
        doThrow(new GoogleAuthException()).when(sync).syncAll("album", file, null);

        SyncTask task = Mockito.spy(new SyncTask(presenter, sync));

        task.doInBackground();

        verify(task).markAsError(    "com.google.android.gms.auth.GoogleAuthException");
    }

}