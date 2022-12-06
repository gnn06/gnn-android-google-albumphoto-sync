package gnn.com.googlealbumdownloadappnougat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerTask;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.Photo;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.sync.PersistSyncTime;
import gnn.com.photos.sync.SyncData;

public class UITextHelperTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private MainActivity activityMock;
    private PersistSyncTime persistSyncTime;
    private File folder;

    @Before
    public void setUp() throws Exception {
        activityMock = mock(MainActivity.class);
        folder = temporaryFolder.newFolder();
        persistSyncTime = new PersistSyncTime(folder);
    }

    @Test
    public void getDetailResultText_null() {
        // Given
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        // When
        String resultText = UITextHelper.getDetailResultText(null, true);
        System.out.println(resultText);

        assertThat(resultText, is(""));
    }

    @Test
    public void getDetailResultText_void() {
        // Given
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        SyncData syncData = new SyncData();
        // When
        String resultText = UITextHelper.getDetailResultText(syncData, true);
        System.out.println(resultText);

        assertThat(resultText, is(""));
    }

    @Test
    public void getDetailResultText_retrieved() throws IOException {
        // Given
        FileUtils.write(new File(folder, PersistSyncTime.FILENAME), "{\"albumSize\":12,\"deleteCount\":24,\"downloadCount\":48}", "UTF-8");
        SyncData syncData = persistSyncTime.retrieveSyncResult();
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        // When
        String resultText = UITextHelper.getDetailResultText(syncData, true);
        System.out.println(resultText);
        String expected = "album size = 12\ndownloaded = 48\ndeleted = 24";
        // Then
        assertThat(resultText, is(expected));
    }

    @Test
    public void getDetailResultText_list_over_number() {
        // Given
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        // When
        ArrayList<Photo> downloadLst = new ArrayList<>(Collections.singletonList(new Photo("url1", "id1")));
        ArrayList<Photo> deleteLst = new ArrayList<>(Arrays.asList(new Photo("url2", "id2"), new Photo("url3", "id3")));
        SyncData syncData = new SyncData(12, 24, 48, downloadLst, deleteLst, 3, 6);
        String resultText = UITextHelper.getDetailResultText(syncData, true);
        System.out.println(resultText);
        String expected = "album size = 12\ndownloaded = 1\ndeleted = 2";
        // Then
        assertThat(resultText, is(expected));
    }

    @Test
    public void getDetailResultText_inProgress() {
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
    public void getDetailResultText_finish() {
        UITextHelper UITextHelper = new UITextHelper(activityMock);
        SyncData synchronizer = new SyncData();
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
    public void getResultString_null() {
        // Given
        Resources resources = mock(Resources.class);
        when(activityMock.getResources()).thenReturn(resources);
        when(resources.getString(anyInt())).thenReturn("no result-foo");

        UITextHelper UITextHelper = new UITextHelper(activityMock);
        // When
        String resultText = UITextHelper.getResultString(null, SyncStep.STARTING, activityMock);
        System.out.println(resultText);

        assertThat(resultText, is("no result-foo"));
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
