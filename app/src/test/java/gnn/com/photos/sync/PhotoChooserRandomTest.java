package gnn.com.photos.sync;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import gnn.com.photos.model.Photo;

@RunWith(MockitoJUnitRunner.class)
public class PhotoChooserRandomTest {

    @InjectMocks PhotoChooser chooser;

    @Mock
    ThreadLocalRandom randomizer;

    @Test
    public void choose_same_element_twice() {
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url1", "id1"), new Photo("url2","id2"), new Photo("url3","id3")));
        when(randomizer.nextInt(3))
                .thenReturn(0)
                .thenReturn(0)
                .thenReturn(2);
        ArrayList<Photo> result = chooser.chooseOneList(photos, 2);
        assertEquals(2, result.size());
        assertNotEquals(result.get(0), result.get(1));
    }
}
