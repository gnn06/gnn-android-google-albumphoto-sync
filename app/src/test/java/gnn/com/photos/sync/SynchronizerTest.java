package gnn.com.photos.sync;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;

public class SynchronizerTest {

    @Test
    public void incDownloadCurrent() {
        Presenter.SyncTask syncTaskSpy = Mockito.mock(Presenter.SyncTask.class);
        Synchronizer synchronizerSpy = new Synchronizer(syncTaskSpy);
        synchronizerSpy.incCurrentDownload();
        Mockito.verify(syncTaskSpy, Mockito.times(1)).publicPublish();
    }
}