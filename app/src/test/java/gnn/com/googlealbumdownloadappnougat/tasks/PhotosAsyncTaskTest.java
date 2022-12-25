package gnn.com.googlealbumdownloadappnougat.tasks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.photos.service.RemoteException;

public class PhotosAsyncTaskTest {

    @Mock
    private PersistOauthError persistOauth;

    @Test
    public void doInBackground_Error() {
        IPresenterHome presenter = Mockito.mock(PresenterHome.class);
        SynchronizerAndroid sync = Mockito.mock(SynchronizerAndroid.class);

        PhotosAsyncTask task = new PhotosAsyncTask(presenter, null, persistOauth) {
            @Override
            public Object returnFailure() {
                return null;
            }

            @Override
            public Object execOauthImpl() throws RemoteException {
                return null;
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };

        task.markAsError("message");

        task.onPostExecute(null);

        verify(presenter).showError("message");
        verify(presenter, never()).setSyncResult(any(), any());
    }

}
