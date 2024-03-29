package gnn.com.photos.sync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.Photo;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserListRandomTest {

    @InjectMocks
    PhotoChooserList chooser;

    @Mock
    ThreadLocalRandom randomiser;

    @Before
    public void setUp() throws Exception {
        Logger.configure();
    }

    @Test
    public void choose_same_element_twice() {
        ArrayList<Photo> photos = createPhotoList(3);
        ArrayList<Photo> result = chooser.chooseOneList(photos, 2, null);
        Assert.assertEquals(2, result.size());
        assertNotEquals(result.get(0), result.get(1));
    }

    @Test
    public void choose_not_previous_first() {
        final ArrayList<Photo> photos = createPhotoList(100);
        final ArrayList<Photo> previousPhotos = createRandomPhotoList(25, 100);
        // when
        ArrayList<Photo> result = chooser.chooseOneList(photos, 25, previousPhotos);
        // then
        List<Photo> intersectPreviousCurrentList = result.stream().filter(item -> previousPhotos.contains(item)).collect(Collectors.toList());
        assertEquals(0, intersectPreviousCurrentList.size());
    }

    @Test
    public void choose_not_previous_need_duplication() {
        final ArrayList<Photo> photos = createPhotoList(100);
        final ArrayList<Photo> previousPhotos = createRandomPhotoList(90, 100);
        // when
        ArrayList<Photo> result = chooser.chooseOneList(photos, 90, previousPhotos);
        // then
        assertNotEquals(0, result.size());
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
