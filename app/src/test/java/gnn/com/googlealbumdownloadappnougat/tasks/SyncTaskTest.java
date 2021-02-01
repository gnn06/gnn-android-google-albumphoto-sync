package gnn.com.googlealbumdownloadappnougat.tasks;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.Presenter;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyncTaskTest {

    @Test
    public void doInBackground_Error() throws IOException, GoogleAuthException {
        IPresenter presenter = Mockito.mock(Presenter.class);
        SynchronizerAndroid sync = Mockito.mock(SynchronizerAndroid.class);
        File file = Mockito.mock(File.class);

        when(presenter.getAlbum()).thenReturn("album");
        when(((Presenter) presenter).getFolder()).thenReturn(file);
        doThrow(new GoogleAuthException()).when(sync).syncAll("album", file);

        SyncTask task = Mockito.spy(new SyncTask(presenter, sync));

        task.doInBackground();

        verify(task).markAsError();
    }

}