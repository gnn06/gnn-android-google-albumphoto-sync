package gnn.com.photos.sync;

import org.junit.Test;

import java.util.ArrayList;

import gnn.com.photos.model.Photo;

import static org.junit.Assert.*;

public class PhotoChooserTest {

    @Test
    public void chooseOne() {
        ArrayList<Photo> remote = new ArrayList<>();
        remote.add(new Photo("url1", "id1"));
        remote.add(new Photo("url2", "id2"));
        remote.add(new Photo("url3", "id3"));

        ArrayList<Photo> result1 = PhotoChooser.chooseOneList(remote);

        assertEquals(1, result1.size());
        System.out.print(result1.get(0).getId());
    }

    @Test
    public void firstMinusSecond() {
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

}