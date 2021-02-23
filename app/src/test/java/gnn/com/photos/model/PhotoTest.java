package gnn.com.photos.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PhotoTest {

    @Test
    public void idFromPhoto() {
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(new Photo("url", "id")));
        ArrayList<String> actual = Photo.IdFromPhoto(photos);
        String[] expected = {"id"};
        assertArrayEquals(expected, actual.toArray());
    }
}