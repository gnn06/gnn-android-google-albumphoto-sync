package gnn.com.googlealbumdownloadappnougat.async;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.sync.Synchronizer;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class PhotosAsyncTaskTest {

    @Test
    public void doInBackground_Error() {
        IPresenter presenter = Mockito.mock(Presenter.class);
        Synchronizer sync = Mockito.mock(Synchronizer.class);

        PhotosAsyncTask task = new PhotosAsyncTask(presenter) {
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        };

        task.markAsError();

        task.onPostExecute(null);

        verify(presenter).showError();
        verify(presenter, never()).updateUI_CallResult(sync, SyncStep.FINISHED);
    }

}