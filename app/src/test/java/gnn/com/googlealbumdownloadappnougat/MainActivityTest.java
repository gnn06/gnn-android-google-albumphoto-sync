package gnn.com.googlealbumdownloadappnougat;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.model.Photo;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

import static org.junit.Assert.assertEquals;

public class MainActivityTest {

    @Test
    public void getResultText_inProgress() {
        SyncTask syncTaskSpy = Mockito.mock(SyncTask.class);
        MainActivity activity = new MainActivity();
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(activity);
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
        Whitebox.setInternalState(synchronizer, "albumSize", 2);

        String resultText = activity.getResultText(synchronizer,false);

        assertEquals("album size = 2\ndownloaded = 2 / 3\ndeleted = 1 / 1", resultText);
    }

    @Test
    public void getResultText_finish() {
        SyncTask syncTaskSpy = Mockito.mock(SyncTask.class);
        MainActivity activity = new MainActivity();
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(activity);
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
        Whitebox.setInternalState(synchronizer, "albumSize", 2);

        String resultText = activity.getResultText(synchronizer,true);

        assertEquals("album size = 2\ndownloaded = 3\ndeleted = 1", resultText);
    }

}