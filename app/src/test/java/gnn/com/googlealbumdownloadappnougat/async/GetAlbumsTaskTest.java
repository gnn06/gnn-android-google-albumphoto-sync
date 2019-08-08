package gnn.com.googlealbumdownloadappnougat.async;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.remote.PhotosRemoteService;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAlbumsTaskTest {

    @Test
    public void doInBackground_Error() throws IOException, GoogleAuthException {
        Presenter presenter = Mockito.mock(Presenter.class);
        PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);

        when(prs.getAlbums()).thenThrow(new GoogleAuthException("test"));

        GetAlbumsTask task = Mockito.spy(new GetAlbumsTask(presenter, prs));

        ArrayList<String> result = task.doInBackground();

        assertNull(result);
        verify(task).markAsError();
    }

}