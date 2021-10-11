package gnn.com.googlealbumdownloadappnougat.tasks;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterMain;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Ignore
public class PhotosAsyncTaskTest {

    @Test
    public void doInBackground_Error() {
        IPresenterMain presenter = Mockito.mock(PresenterMain.class);
        SynchronizerAndroid sync = Mockito.mock(SynchronizerAndroid.class);

        PhotosAsyncTask task = new PhotosAsyncTask(presenter) {
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };

        task.markAsError("message");

        task.onPostExecute(null);

        verify(presenter).showError("message");
        verify(presenter, never()).setSyncResult(sync, SyncStep.FINISHED);
    }

}