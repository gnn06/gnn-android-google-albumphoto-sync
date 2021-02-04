package gnn.com.googlealbumdownloadappnougat;

import android.content.res.Resources;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.model.Photo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UITextHelperTest {

    @Test
    public void getResultText_inProgress() {
        SyncTask syncTaskSpy = Mockito.mock(SyncTask.class);
        MainActivity activity = new MainActivity();
        UITextHelper UITextHelper = new UITextHelper(activity);
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(activity, null, 24 * 60 * 60 * 1000, null);
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

        String resultText = UITextHelper.getDetailResultText(synchronizer, false);

        assertEquals("album size = 2\ndownloaded = 2 / 3\ndeleted = 1 / 1", resultText);
    }

    @Test
    public void getResultText_finish() {
        SyncTask syncTaskSpy = Mockito.mock(SyncTask.class);
        MainActivity activity = new MainActivity();
        UITextHelper UITextHelper = new UITextHelper(activity);
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(activity, null, 24 * 60 * 60 * 1000, null);
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

        String resultText = UITextHelper.getDetailResultText(synchronizer,true);

        assertEquals("album size = 2\ndownloaded = 3\ndeleted = 1", resultText);
    }

    @Test
    public void getLastSyncTimeString_null() {
        MainActivity activity = mock(MainActivity.class);

        Resources ressources = mock(Resources.class);
        when(activity.getResources()).thenReturn(ressources);
        when(ressources.getString(anyInt())).thenReturn("nono");

        UITextHelper uiTextHelper = new UITextHelper(activity);

        String actual = uiTextHelper.getLastSyncTimeString(null);

        assertEquals("nono", actual);
    }
}