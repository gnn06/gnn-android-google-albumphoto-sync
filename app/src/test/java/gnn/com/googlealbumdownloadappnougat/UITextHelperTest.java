package gnn.com.googlealbumdownloadappnougat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerTask;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.Photo;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.sync.Temp;

public class UITextHelperTest {

    private MainActivity activityMock;

    @Before
    public void setUp() throws Exception {
        activityMock = mock(MainActivity.class);
    }

    @Test
    public void getResultText_void() {
        // Given
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        SynchronizerTask synchronizer = new SynchronizerTask(activityMock, null, 0, null);
        Temp syncData = new Temp();
        // When
        String resultText = UITextHelper.getDetailResultText(syncData, true);
        System.out.println(resultText);

        assertThat(resultText, is(""));
    }

    @Test
    public void getResultText_inProgress() {
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        SynchronizerTask synchronizer = new SynchronizerTask(activityMock, null, 24 * 60 * 60 * 1000, null);
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

        String resultText = UITextHelper.getDetailResultText(synchronizer.getSyncData(), false);

        assertEquals("album size = 2\ndownloaded = 2 / 3\ndeleted = 1 / 1", resultText);
    }

    @Test
    public void getResultText_finish() {
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        Temp synchronizer = new Temp();
        ArrayList<Photo> toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("aze", "12"));
        toDownloadList.add(new Photo("ZER", "13"));
        toDownloadList.add(new Photo("sqd", "34"));
        ArrayList<Photo> toDeleteList = new ArrayList<>();
        toDeleteList.add(new Photo("aze", "23"));
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
    public void getResultText_number_versus_list() {
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        Temp synchronizer = new Temp(208, 4, 4, new ArrayList<>(), new ArrayList<>(), 0, 0);

        String resultText = UITextHelper.getDetailResultText(synchronizer, true);

        assertEquals("album size = 208\ndownloaded = 4\ndeleted = 4", resultText);
    }

    @Test
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
    public void stat_with_dayBefore() {
        UITextHelper helper = new UITextHelper(activityMock);
        String text = helper.getStat(new WallpaperStat(12, 14, new Date(74, 3-1, 9, 12, 00)));
        assertThat(text, is("nombre de changement 14 la veille du 9 mars 1974"));
    }

    @Test
    public void stat_null() {
        UITextHelper helper = new UITextHelper(activityMock);
        String text = helper.getStat(null);
        assertThat(text, is("Aucune stat."));
    }

    @Test
    public void stat_date_null() {
        UITextHelper helper = new UITextHelper(activityMock);
        String text = helper.getStat(new WallpaperStat(0, 0, null));
        assertThat(text, is("Aucune stat."));
    }
}
