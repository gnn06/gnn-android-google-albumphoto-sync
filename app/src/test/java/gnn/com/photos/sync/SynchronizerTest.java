package gnn.com.photos.sync;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.model.Photo;

import static org.junit.Assert.assertEquals;

public class SynchronizerTest {

    @Test
    public void incDownloadCurrent() {
        Presenter.SyncTask syncTaskSpy = Mockito.mock(Presenter.SyncTask.class);
        Synchronizer synchronizerSpy = new Synchronizer(syncTaskSpy);
        synchronizerSpy.incCurrentDownload();
        Mockito.verify(syncTaskSpy, Mockito.times(1)).publicPublish();
    }

    @Test
    public void getResultText_inProgress() {
        Presenter.SyncTask syncTaskSpy = Mockito.mock(Presenter.SyncTask.class);
        Synchronizer synchronizer = new Synchronizer(syncTaskSpy);
        ArrayList<Photo> toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("aze","12"));
        toDownloadList.add(new Photo("ZER","13"));
        toDownloadList.add(new Photo("sqd","34"));
        ArrayList<Photo> toDeleteList = new ArrayList<>();
        toDeleteList.add(new Photo("aze", "23"));
        Whitebox.setInternalState(synchronizer, "toDownload", toDownloadList);
        Whitebox.setInternalState(synchronizer, "toDelete", toDeleteList);
        Whitebox.setInternalState(synchronizer, "currentDownload", 2);
        Whitebox.setInternalState(synchronizer, "currentDelete", 1);

        String resultText = synchronizer.getResultText(false);

        assertEquals("downloaded = 2 / 3\ndeleted = 1 / 1", resultText);
    }

    @Test
    public void getResultText_finish() {
        Presenter.SyncTask syncTaskSpy = Mockito.mock(Presenter.SyncTask.class);
        Synchronizer synchronizer = new Synchronizer(syncTaskSpy);
        ArrayList<Photo> toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("aze","12"));
        toDownloadList.add(new Photo("ZER","13"));
        toDownloadList.add(new Photo("sqd","34"));
        ArrayList<Photo> toDeleteList = new ArrayList<>();
        toDeleteList.add(new Photo("aze", "23"));
        Whitebox.setInternalState(synchronizer, "toDownload", toDownloadList);
        Whitebox.setInternalState(synchronizer, "toDelete", toDeleteList);
        Whitebox.setInternalState(synchronizer, "currentDownload", 2);
        Whitebox.setInternalState(synchronizer, "currentDelete", 1);

        String resultText = synchronizer.getResultText(true);

        assertEquals("downloaded = 3\ndeleted = 1", resultText);
    }
}