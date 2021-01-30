package gnn.com.photos.sync;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosRemoteService;

import static org.junit.Assert.*;

public class PhotoChooserTest {

    @Test
    public void chooseOneList() {
        ArrayList<Photo> remote = new ArrayList<>();
        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));

        ArrayList<Photo> result1 = PhotoChooser.chooseOneList(remote);

        assertEquals(1, result1.size());
        System.out.print(result1.get(0).getId());
    }

    @Test
    public void firstMinusSecondList() {
        ArrayList<Photo> remote = new ArrayList<>();
        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));

        ArrayList<Photo> local = new ArrayList<>();
        local.add(new Photo("url3", "id3"));

        ArrayList<Photo> result = PhotoChooser.firstMinusSecondList(remote, local);

        assertEquals(2, result.size());
        assertFalse(result.contains(new Photo("url3", "id3")));
    }

    @Test
    public void chooseOne() {
        Synchronizer syncData = new Synchronizer(null, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return null;
            }
            @Override
            public void incAlbumSize() {

            }
        };
        ArrayList<Photo> local = new ArrayList<>(
                Collections.singletonList(
                        new Photo("url1", "id1")));
        ArrayList<Photo> remote = new ArrayList<>(
                Arrays.asList(
                        new Photo("url1", "id1"),
                        new Photo("url2", "id2"),
                        new Photo("url3", "id3")
                ));

        // when
        PhotoChooser.chooseRandom(syncData, local, remote);

        // then
        assertEquals(1, syncData.getToDelete().size());
        assertEquals(1, syncData.getToDownload().size());
    }
}