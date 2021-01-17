package gnn.com.photos.remote;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.model.Photo;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {

    @Test
    public void test_empty_no_persistence() throws IOException {
        // given an empty cache
        Cache cache = new Cache(null);
        // when get cache
        Object result = cache.get();
        // then, check that get an answer
        assertNull(result);
    }

    @Test
    public void test_notEmpty_no_persistence() throws IOException {
        // given an not empty cache
        Cache cache = new Cache(null);
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1")
        ));
        cache.put(photos);
        // when get cache
        Object result = cache.get();
        // then, check that get an answer
        assertEquals(photos, result);
    }

    @Test
    public void test_put_noPersistence() throws IOException {
        // given empty cache
        Cache cache = new Cache(null);
        assertNull(cache.get());
        // when
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1")
        ));
        cache.put(photos);
        // then
        assertEquals(photos, cache.get());
    }

    @Test
    public void test_put_persistent() throws IOException {
        // given empty cache
        File file = mock(File.class);
        Cache cache = spy(new Cache(file));
        doNothing().when(cache).write();
        assertNull(cache.get());
        // when
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1")
        ));
        cache.put(photos);
        // then value was put into cache
        assertEquals(photos, cache.get());
        // then write was call
        verify(cache, times(1)).write();
    }

    @Test
    public void test_empty_from_disk() throws IOException {
        // given an not empty cache
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        Cache cache = spy(new Cache(file));
        ArrayList<Photo> photos = new ArrayList<>(Arrays.asList(
                new Photo("url1", "id1")
        ));
        doReturn(photos).when(cache).read();
        // when get cache
        Object result = cache.get();
        // then, check that get an answer
        assertEquals(photos, result);
    }
}
