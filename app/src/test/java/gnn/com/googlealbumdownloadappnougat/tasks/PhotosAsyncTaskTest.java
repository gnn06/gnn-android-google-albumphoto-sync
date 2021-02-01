package gnn.com.googlealbumdownloadappnougat.tasks;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.Presenter;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class PhotosAsyncTaskTest {

    @Test
    public void doInBackground_Error() {
        IPresenter presenter = Mockito.mock(Presenter.class);
        SynchronizerAndroid sync = Mockito.mock(SynchronizerAndroid.class);

        PhotosAsyncTask task = new PhotosAsyncTask(presenter) {
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };

        task.markAsError();

        task.onPostExecute(null);

        verify(presenter).showError();
        verify(presenter, never()).setSyncResult(sync, SyncStep.FINISHED);
    }

}