package gnn.com.googlealbumdownloadappnougat;

import android.content.res.Resources;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.Photo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UITextHelperTest {

    private MainActivity activityMock;

    @Before
    public void setUp() throws Exception {
        activityMock = mock(MainActivity.class);
    }

    @Test
    @Ignore
    public void getResultText_inProgress() {
        MainActivity activity = new MainActivity();
        UITextHelper UITextHelper = new UITextHelper(activity);
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(activity, null, 24 * 60 * 60 * 1000, null);
        ArrayList<Photo> toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("aze", "12"));
        toDownloadList.add(new Photo("ZER", "13"));
        toDownloadList.add(new Photo("sqd", "34"));
        ArrayList<Photo> toDeleteList = new ArrayList<>();
        toDeleteList.add(new Photo("aze", "23"));
        synchronizer.setToDownload(toDownloadList);
        synchronizer.setToDelete(toDeleteList);
        synchronizer.setSyncTask(mock(SyncTask.class));
        synchronizer.incCurrentDownload();
        synchronizer.incCurrentDownload();
        synchronizer.incCurrentDelete();
        synchronizer.incAlbumSize();
        synchronizer.incAlbumSize();

        String resultText = UITextHelper.getDetailResultText(synchronizer, false);

        assertEquals("album size = 2\ndownloaded = 2 / 3\ndeleted = 1 / 1", resultText);
    }

    @Test
    @Ignore
    public void getResultText_finish() {
        MainActivity activity = new MainActivity();
        UITextHelper UITextHelper = new UITextHelper(activity);
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(activity, null, 24 * 60 * 60 * 1000, null);
        ArrayList<Photo> toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("aze", "12"));
        toDownloadList.add(new Photo("ZER", "13"));
        toDownloadList.add(new Photo("sqd", "34"));
        ArrayList<Photo> toDeleteList = new ArrayList<>();
        toDeleteList.add(new Photo("aze", "23"));
        synchronizer.setSyncTask(mock(SyncTask.class));
        synchronizer.setToDownload(toDownloadList);
        synchronizer.setToDelete(toDeleteList);
        synchronizer.incCurrentDownload();
        synchronizer.incCurrentDownload();
        synchronizer.incCurrentDelete();
        synchronizer.incAlbumSize();
        synchronizer.incAlbumSize();

        String resultText = UITextHelper.getDetailResultText(synchronizer, true);

        assertEquals("album size = 2\ndownloaded = 3\ndeleted = 1", resultText);
    }

    @Test
    @Ignore
    public void getLastSyncTimeString_null_null() {
        MainActivity activity = mock(MainActivity.class);

        Resources ressources = mock(Resources.class);
        when(activity.getResources()).thenReturn(ressources);
        when(ressources.getString(anyInt())).thenReturn("nono");

        UITextHelper uiTextHelper = new UITextHelper(activity);

        String actual = uiTextHelper.getLastTimesString(null, null);

        assertEquals("nono", actual);
    }

    @Test
    public void getLastSyncTimeString_sync_null() {
        MainActivity activity = mock(MainActivity.class);

        Resources ressources = mock(Resources.class);
        when(activity.getResources()).thenReturn(ressources);
        when(ressources.getString(anyInt(), any())).thenReturn("nono");

        UITextHelper uiTextHelper = new UITextHelper(activity);

        String actual = uiTextHelper.getLastTimesString("01/01/2021", null);

        assertEquals("nono", actual);
    }

    @Test
    public void getLastSyncTimeString_sync_wall() {
        MainActivity activity = activityMock;

        Resources ressources = mock(Resources.class);
        when(activity.getResources()).thenReturn(ressources);
        when(ressources.getString(anyInt(), any()))
                .thenReturn("nono")
                .thenReturn("nini");

        UITextHelper uiTextHelper = new UITextHelper(activity);

        String actual = uiTextHelper.getLastTimesString("01/01/2021", "01/02/2021");

        assertEquals("nono\nnini", actual);
    }

    @Test
    public void stat() {
        UITextHelper helper = new UITextHelper(activityMock);
        String text = helper.getStat(new WallpaperStat(12, 14, new Date(74, 3-1, 9, 12, 00)));
        assertThat(text, Is.is("nombre de changement 12 le 9 mars 1974"));
    }
}