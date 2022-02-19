package gnn.com.photos;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class PhotoTest {

    @Test
    public void idFromPhoto() {
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url", "id")));
        ArrayList<String> actual = Photo.IdFromPhoto(photos);
        String[] expected = {"id"};
        Assert.assertArrayEquals(expected, actual.toArray());
    }
}