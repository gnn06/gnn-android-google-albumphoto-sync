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
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserListTest {

    ArrayList<Photo> remote;

    @Before
    public void init() {
        Logger.configure();
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
        ArrayList<Photo> result1 = new PhotoChooserList().chooseOneList(remote, 1, null);

        Assert.assertEquals(1, result1.size());
    }

    @Test
    public void chooseOne_2() {
        ArrayList<Photo> result1 = new PhotoChooserList().chooseOneList(remote, 2, null);

        Assert.assertEquals(2, result1.size());
    }

    @Test
    public void chooseOne_max() {
        ArrayList<Photo> result1 = new PhotoChooserList().chooseOneList(remote, 5, null);

        Assert.assertEquals(3, result1.size());
    }

    @Test
    public void chooseOne_size() {
        ArrayList<Photo> result1 = new PhotoChooserList().chooseOneList(remote, 3, null);

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

        ArrayList<Photo> result = new PhotoChooserList().firstMinusSecondList(remote, local);

        Assert.assertEquals(2, result.size());
        Assert.assertFalse(result.contains(new Photo("url3", "id3")));
    }

    private ArrayList<Photo> createPhotoList(int size) {
        ArrayList result = new ArrayList(size);
        for (int i = 1; i <= size; i++) {
            result.add(new Photo("url" + i, "id" + i));
        }
        return result;
    }

    private ArrayList<Photo> createRandomPhotoList(int size, int bound) {
        ThreadLocalRandom randomiser = ThreadLocalRandom.current();
        ArrayList result = new ArrayList(size);
        while (result.size() < size) {
            int i = randomiser.nextInt(bound);
            Photo photo = new Photo("url" + i, "id" + i);
            if (!result.contains(photo)) {
                result.add(photo);
            }
        }
        return result;
    }
}
