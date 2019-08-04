package gnn.com.photos.sync;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.presenter.SyncTask;

public class SynchronizerTest {

    @Test
    public void incDownloadCurrent() {
        SyncTask syncTaskSpy = Mockito.mock(SyncTask.class);
        MainActivity activity = Mockito.mock(MainActivity.class);
        Synchronizer synchronizerSpy = new Synchronizer(activity);
        synchronizerSpy.setSyncTask(syncTaskSpy);
        synchronizerSpy.incCurrentDownload();
        Mockito.verify(syncTaskSpy, Mockito.times(1)).publicPublish();
    }


}