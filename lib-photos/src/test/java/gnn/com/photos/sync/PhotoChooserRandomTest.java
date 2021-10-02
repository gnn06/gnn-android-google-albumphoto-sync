package gnn.com.photos.sync;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import gnn.com.photos.Photo;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserRandomTest {

    @InjectMocks
    PhotoChooser chooser;

    @Mock
    ThreadLocalRandom randomiser;

    @Test
    public void choose_same_element_twice() {
        ArrayList<Photo> photos = createPhotoList(3);
        Mockito.when(randomiser.nextInt(3))
                .thenReturn(0)
                .thenReturn(0)
                .thenReturn(2);
        ArrayList<Photo> result = chooser.chooseOneList(photos, 2, null, null);
        Assert.assertEquals(2, result.size());
        assertNotEquals(result.get(0), result.get(1));
    }

    @Test
    public void choose_not_previous_first() {
        final ArrayList<Photo> photos = createPhotoList(100);
        final ArrayList<Photo> previousPhotos = createRandomPhotoList(25, 100);
        Mockito.when(randomiser.nextInt(Mockito.anyInt()))
                .thenCallRealMethod();
        // when
        ArrayList<Photo> result = chooser.chooseOneList(photos, 25, previousPhotos, null);
        // then
        List<Photo> intersectPreviousCurrentList = result.stream().filter(item -> previousPhotos.contains(item)).collect(Collectors.toList());
        assertEquals(0, intersectPreviousCurrentList.size());
    }

    @Test
    public void choose_not_previous_need_duplication() {
        final ArrayList<Photo> photos = createPhotoList(100);
        final ArrayList<Photo> previousPhotos = createRandomPhotoList(90, 100);
        Mockito.when(randomiser.nextInt(Mockito.anyInt()))
                .thenCallRealMethod();
        // when
        ArrayList<Photo> result = chooser.chooseOneList(photos, 90, previousPhotos, null);
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
