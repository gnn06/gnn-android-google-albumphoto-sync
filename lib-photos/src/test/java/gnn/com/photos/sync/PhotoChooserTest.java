package gnn.com.photos.sync;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.Photo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserTest {

    ArrayList<Photo> remote;

    @Before
    public void init() {
        remote = new ArrayList<>();
        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));
    }

    @After
    public void after() {
        remote.clear();
    }


    @Test
    public void chooseOne_1() {
        ArrayList<Photo> result1 = new PhotoChooser().chooseOneList(remote, 1);

        Assert.assertEquals(1, result1.size());
    }

    @Test
    public void chooseOne_2() {
        ArrayList<Photo> result1 = new PhotoChooser().chooseOneList(remote, 2);

        Assert.assertEquals(2, result1.size());
    }

    @Test
    public void chooseOne_max() {
        ArrayList<Photo> result1 = new PhotoChooser().chooseOneList(remote, 5);

        Assert.assertEquals(3, result1.size());
    }

    @Test
    public void chooseOne_size() {
        ArrayList<Photo> result1 = new PhotoChooser().chooseOneList(remote, 3);

        Assert.assertEquals(3, result1.size());
    }

    @Test
    public void firstMinusSecond() {
        ArrayList<Photo> remote = new ArrayList<>();
        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));

        ArrayList<Photo> local = new ArrayList<>();
        local.add(new Photo("url3", "id3"));

        ArrayList<Photo> result = new PhotoChooser().firstMinusSecondList(remote, local);

        Assert.assertEquals(2, result.size());
        Assert.assertFalse(result.contains(new Photo("url3", "id3")));
    }

    @Test
    public void choosen_nothing_to_download_and_to_delete() {
        PhotoChooser chooser = Mockito.spy(PhotoChooser.class);
        SyncData syncData = Mockito.spy(SyncData.class);

        ArrayList<Photo> remote = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1"),
                new Photo("url2", "id2"),
                new Photo("url3", "id3"),
                new Photo("url4", "id4")));
        ArrayList<Photo> choosen = new ArrayList<>(Arrays.asList(
                new Photo("url2", "id2"),
                new Photo("url4", "id4")));
        ArrayList<Photo> local = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id2"),
                new Photo("url3", "id4")));
        ArrayList<Photo> expectedToDownload = new ArrayList<>();
        ArrayList<Photo> expectedToDelete = new ArrayList<>();

        Mockito.when(chooser.chooseOneList(remote, 2)).thenReturn(choosen);

        chooser.chooseRandom(syncData, local, remote, null, 2);

        Mockito.verify(syncData).setToDownload(expectedToDownload);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choosen_all_to_download_and_all_to_delete() {
        PhotoChooser chooser = Mockito.spy(PhotoChooser.class);
        SyncData syncData = Mockito.spy(SyncData.class);

        ArrayList<Photo> remote = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1"),
                new Photo("url2", "id2"),
                new Photo("url3", "id3"),
                new Photo("url4", "id4")));
        ArrayList<Photo> choosen = new ArrayList<>(Arrays.asList(
                new Photo("url2", "id3"),
                new Photo("url4", "id4")));
        ArrayList<Photo> local = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1"),
                new Photo("url3", "id2")));
        ArrayList<Photo> expectedToDownload = new ArrayList<>(Arrays.asList(
                new Photo("url2", "id3"),
                new Photo("url4", "id4")));
        ArrayList<Photo> expectedToDelete = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1"),
                new Photo("url3", "id2")));

        Mockito.doCallRealMethod().when(chooser).chooseRandom(syncData, local, remote, null, 2);

        Mockito.when(chooser.chooseOneList(remote, 2)).thenReturn(choosen);

        chooser.chooseRandom(syncData, local, remote, null, 2);

        Mockito.verify(syncData).setToDownload(expectedToDownload);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }

    @Test
    public void choosen_something_to_download_and_something_to_delete() {
        PhotoChooser chooser = Mockito.spy(PhotoChooser.class);
        SyncData syncData = Mockito.spy(SyncData.class);

        ArrayList<Photo> remote = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1"),
                new Photo("url2", "id2"),
                new Photo("url3", "id3"),
                new Photo("url4", "id4")));
        ArrayList<Photo> choosen = new ArrayList<>(Arrays.asList(
                new Photo("url2", "id2"),
                new Photo("url4", "id4")));
        ArrayList<Photo> local = new ArrayList<>(Arrays.asList(
                new Photo("url2", "id1"),
                new Photo("url4", "id2")));
        ArrayList<Photo> expectedToDownload = new ArrayList<>(Arrays.asList(
                new Photo("url4", "id4")));
        ArrayList<Photo> expectedToDelete = new ArrayList<>(Arrays.asList(
                new Photo("url4", "id1")));

        Mockito.doCallRealMethod().when(chooser).chooseRandom(syncData, local, remote, null, 2);

        Mockito.when(chooser.chooseOneList(remote, 2)).thenReturn(choosen);

        chooser.chooseRandom(syncData, local, remote, null, 2);

        Mockito.verify(syncData).setToDownload(expectedToDownload);
        Mockito.verify(syncData).setToDelete(expectedToDelete);
    }

}