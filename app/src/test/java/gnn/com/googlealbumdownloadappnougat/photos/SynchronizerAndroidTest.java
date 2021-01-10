package gnn.com.googlealbumdownloadappnougat.photos;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.async.SyncTask;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public class SynchronizerAndroidTest {

    @Test
    public void incDownloadCurrent() {
        SyncTask syncTaskSpy = Mockito.mock(SyncTask.class);
        MainActivity activity = Mockito.mock(MainActivity.class);
        SynchronizerAndroid synchronizerSpy = new SynchronizerAndroid(activity);
        synchronizerSpy.setSyncTask(syncTaskSpy);
        synchronizerSpy.incCurrentDownload();
        Mockito.verify(syncTaskSpy, Mockito.times(1)).publicPublish();
    }


}