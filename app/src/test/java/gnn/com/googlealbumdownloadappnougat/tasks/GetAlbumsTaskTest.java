package gnn.com.googlealbumdownloadappnougat.tasks;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterMain;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAlbumsTaskTest {

    @Test
    public void doInBackground_Error() throws RemoteException {
        PresenterMain presenter = Mockito.mock(PresenterMain.class);
        PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);

        when(prs.getAlbums()).thenThrow(new RemoteException());

        GetAlbumsTask task = Mockito.spy(new GetAlbumsTask(presenter, prs));

        ArrayList<String> result = task.doInBackground();

        assertNull(result);
        verify(task).markAsError("gnn.com.photos.service.RemoteException");
    }

}