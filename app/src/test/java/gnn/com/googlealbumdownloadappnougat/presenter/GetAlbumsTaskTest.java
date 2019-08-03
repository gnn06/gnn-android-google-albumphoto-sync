package gnn.com.googlealbumdownloadappnougat.presenter;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.photos.remote.PhotosRemoteService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAlbumsTaskTest {

    @Test
    public void doInBackground() throws IOException, GoogleAuthException {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.mock(Presenter.class);
        PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);

        ArrayList<String> resultExpected = new ArrayList<>();
        resultExpected.add("id1");

        when(prs.getAlbums()).thenReturn(resultExpected);

        GetAlbumsTask task = new GetAlbumsTask(activity, presenter, prs);
        ArrayList<String> resultActual = task.doInBackground();
        assertFalse(task.error);
        assertEquals(resultExpected, resultActual);

        task.onPostExecute(resultActual);
        verify(activity, never()).showError();
        verify(presenter).setAlbums(resultActual);
    }

    @Test
    public void doInBackground_Error() throws IOException, GoogleAuthException {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.mock(Presenter.class);
        PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);

        when(prs.getAlbums()).thenThrow(new GoogleAuthException("test"));
        GetAlbumsTask task = new GetAlbumsTask(activity, presenter, prs);
        ArrayList<String> result = task.doInBackground();
        assertTrue(task.error);
        assertNull(result);

        task.onPostExecute(null);
        verify(activity).showError();
        verify(presenter, never()).setAlbums(null);
    }

}