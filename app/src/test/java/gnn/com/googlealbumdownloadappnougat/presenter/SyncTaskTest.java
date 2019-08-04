package gnn.com.googlealbumdownloadappnougat.presenter;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.photos.sync.Synchronizer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SyncTaskTest {

    @Test
    public void doInBackground() throws IOException, GoogleAuthException {
        IPresenter presenter = Mockito.mock(Presenter.class);
        MainActivity activity = Mockito.mock(MainActivity.class);
        Synchronizer sync = Mockito.mock(Synchronizer.class);
        File file = Mockito.mock(File.class);

        when(presenter.getAlbum()).thenReturn("album");
        when(((Presenter) presenter).getFolder()).thenReturn(file);

        SyncTask task = new SyncTask(presenter, sync);

        task.doInBackground();

        assertFalse(task.error);

        task.onPostExecute(null);

        verify(activity, never()).showError();
        verify(presenter).updateUI_CallResult(sync, SyncStep.FINISHED);

    }

    @Test
    public void doInBackground_Error() throws IOException, GoogleAuthException {
        IPresenter presenter = Mockito.mock(Presenter.class);
        MainActivity activity = Mockito.mock(MainActivity.class);
        Synchronizer sync = Mockito.mock(Synchronizer.class);
        File file = Mockito.mock(File.class);

        when(presenter.getAlbum()).thenReturn("album");
        when(((Presenter) presenter).getFolder()).thenReturn(file);
        doThrow(new GoogleAuthException()).when(sync).sync("album", file);

        SyncTask task = new SyncTask(presenter, sync);

        task.doInBackground();

        assertTrue(task.error);

        task.onPostExecute(null);

        verify(presenter).showError();
        verify(presenter, never()).updateUI_CallResult(sync, SyncStep.FINISHED);
    }

}