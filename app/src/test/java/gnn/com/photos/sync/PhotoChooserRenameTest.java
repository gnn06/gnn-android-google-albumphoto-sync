package gnn.com.photos.sync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import gnn.com.photos.model.Photo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserRenameTest {

    private SyncData syncData;
    private ArrayList<Photo> remote;
    private ArrayList<Photo> local;
    private ArrayList<Photo> expectedToDelete;
    private ArrayList<Photo> chosen;

    @Before
    public void init() {
        syncData = mock(SyncData.class);
        remote = new ArrayList<>();
        local = new ArrayList<>();
        expectedToDelete = new ArrayList<>();
        chosen = new ArrayList<>();
    }

    @Test
    public void choose_full_rename_nothing_to_delete() {
        PhotoChooser chooser = new PhotoChooser();

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));
        remote.add(new Photo("url4", "id4"));

        chooser.chooseFull(syncData, local, remote, "name");

        verify(syncData).setToDownload(remote);
        verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_full_rename_delete_remaining() {
        PhotoChooser chooser = new PhotoChooser();

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));
        local.add(new Photo("url3", "name3"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url1", "id2"));

        chooser.chooseFull(syncData, local, remote, "name");

        expectedToDelete.add(new Photo("url1", "name3"));

        verify(syncData).setToDownload(remote);
        verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_random_rename_nothing_to_delete() {
        PhotoChooser chooser = spy(new PhotoChooser());

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));
        remote.add(new Photo("url4", "id4"));

        chosen.add(new Photo("url2", "id2"));
        chosen.add(new Photo("url4", "id4"));

        when(chooser.chooseOneList(remote, 2)).thenReturn(chosen);

        chooser.chooseRandom(syncData, local, remote, "name", 2);

        verify(syncData).setToDownload(chosen);
        verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_random_rename_delete_remaining() {
        PhotoChooser chooser = new PhotoChooser();

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));
        local.add(new Photo("url3", "name3"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url1", "id2"));

        chooser.chooseRandom(syncData, local, remote, "name", 2);

        expectedToDelete.add(new Photo("url1", "name3"));

        verify(syncData).setToDownload(remote);
        verify(syncData).setToDelete(expectedToDelete);
    }
}