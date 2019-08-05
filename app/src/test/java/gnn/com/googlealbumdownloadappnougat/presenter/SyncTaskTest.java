package gnn.com.googlealbumdownloadappnougat.presenter;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import gnn.com.photos.sync.Synchronizer;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyncTaskTest {

    @Test
    public void doInBackground_Error() throws IOException, GoogleAuthException {
        IPresenter presenter = Mockito.mock(Presenter.class);
        Synchronizer sync = Mockito.mock(Synchronizer.class);
        File file = Mockito.mock(File.class);

        when(presenter.getAlbum()).thenReturn("album");
        when(((Presenter) presenter).getFolder()).thenReturn(file);
        doThrow(new GoogleAuthException()).when(sync).sync("album", file);

        SyncTask task = Mockito.spy(new SyncTask(presenter, sync));

        task.doInBackground();

        verify(task).markAsError();
    }

}