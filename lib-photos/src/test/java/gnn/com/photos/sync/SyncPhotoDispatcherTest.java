package gnn.com.photos.sync;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import gnn.com.photos.Photo;

@RunWith(MockitoJUnitRunner.class)
public class SyncPhotoDispatcherTest {

    private SyncData syncData;
    private ArrayList<Photo> remote;
    private ArrayList<Photo> local;
    private ArrayList<Photo> expectedToDelete;
    private ArrayList<Photo> chosen;
    private SyncPhotoDispatcher chooser;
    private PhotoChooserList photoChooserList;

    @Before
    public void init() {
        syncData = mock(SyncData.class);
        remote = new ArrayList<>();
        local = new ArrayList<>();
        expectedToDelete = new ArrayList<>();
        chosen = new ArrayList<>();
        chosen.add(new Photo("url2", "id2"));
        chosen.add(new Photo("url4", "id4"));
        photoChooserList = mock(PhotoChooserList.class);
        when(photoChooserList.chooseOneList(remote, 2, local)).thenReturn(chosen);
        when(photoChooserList.firstMinusSecondList(any(), any())).thenCallRealMethod();
        chooser = new SyncPhotoDispatcher(photoChooserList);
    }

    @Test
    public void choose_full_rename_nothing_to_delete() {

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));
        remote.add(new Photo("url4", "id4"));

        chooser.chooseFull(syncData, local, remote, "name");

        Mockito.verify(syncData).setToDownload(remote);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_full_rename_delete_remaining() {

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));
        local.add(new Photo("url3", "name3"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url1", "id2"));

        chooser.chooseFull(syncData, local, remote, "name");

        expectedToDelete.add(new Photo("url1", "name3"));

        Mockito.verify(syncData).setToDownload(remote);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_random_rename_nothing_to_delete() {

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));
        remote.add(new Photo("url4", "id4"));

        chosen.add(new Photo("url2", "id2"));
        chosen.add(new Photo("url4", "id4"));

        when(photoChooserList.chooseOneList(remote, 2, local)).thenReturn(chosen);

        chooser.chooseRandom(syncData, local, remote, "name", 2);

        Mockito.verify(syncData).setToDownload(chosen);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_random_rename_delete_remaining() {

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));
        local.add(new Photo("url3", "name3"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url1", "id2"));

        ArrayList<Photo> chosen = new ArrayList<>();
        chosen.add(new Photo("url1", "id1"));
        chosen.add(new Photo("url2", "id2"));

        PhotoChooserList photoChooserList = mock(PhotoChooserList.class);
        when(photoChooserList.chooseOneList(remote, 2, local)).thenReturn(chosen);
        when(photoChooserList.firstMinusSecondList(any(), any())).thenCallRealMethod();

        SyncPhotoDispatcher chooser = new SyncPhotoDispatcher(photoChooserList);

        chooser.chooseRandom(syncData, local, remote, "name", 2);

        expectedToDelete.add(new Photo("url1", "name3"));

        Mockito.verify(syncData).setToDownload(remote);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }
}