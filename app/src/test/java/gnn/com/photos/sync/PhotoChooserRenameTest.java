package gnn.com.photos.sync;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.model.Photo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserRenameTest {

    private ArrayList<Photo> remote;
    private ArrayList<Photo> local;
    private SyncData syncData;
    private ArrayList<Photo> expectedToDelete;

    @Before
    public void init() {
        syncData = mock(SyncData.class);
        remote = new ArrayList<>();
        local = new ArrayList<>();
        expectedToDelete = new ArrayList<>();
    }

    @Test
    public void choose_rename_nothing_to_delete() {
        PhotoChooser chooser = new PhotoChooser();

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));
        remote.add(new Photo("url4", "id4"));

        chooser.chooseFull(syncData, local, remote);

        verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choose_rename_delete_remaining() {
        PhotoChooser chooser = new PhotoChooser();

        local.add(new Photo("url1", "name1"));
        local.add(new Photo("url3", "name2"));
        local.add(new Photo("url3", "name3"));

        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url1", "id2"));

        chooser.chooseFull(syncData, local, remote);

        expectedToDelete.add(new Photo("url1", "name3"));

        verify(syncData).setToDelete(expectedToDelete);
    }

}