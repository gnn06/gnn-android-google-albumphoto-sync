package gnn.com.googlealbumdownloadappnougat.tasks;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public class GetAlbumsTaskTest {

    @Test
    public void doInBackground_Error() throws RemoteException {
        PresenterHome presenter = Mockito.mock(PresenterHome.class);
        PhotosRemoteService prs = Mockito.mock(PhotosRemoteService.class);
        Context activity = mock(MainActivity.class);

        when(prs.getAlbums()).thenThrow(new RemoteException(null));

        GetAlbumsTask task = Mockito.spy(new GetAlbumsTask(presenter, prs, activity));

        ArrayList<String> result = task.doInBackground();

        assertNull(result);
        verify(task).markAsError("gnn.com.photos.service.RemoteException");
    }

}
