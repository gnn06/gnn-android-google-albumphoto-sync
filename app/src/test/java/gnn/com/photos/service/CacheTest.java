package gnn.com.photos.service;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import gnn.com.photos.model.Photo;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {

    @Test
    public void test_empty_no_persistence() throws IOException {
        // given an empty cache
        Cache cache = new Cache(null, -1);
        // when get cache
        Object result = cache.get();
        // then, check that get an answer
        assertNull(result);
    }

    @Test
    public void test_notEmpty_no_persistence() throws IOException {
        // given an not empty cache
        Cache cache = new Cache(null, 24 * 60 * 60 * 1000);
        ArrayList<Photo> photos = new ArrayList<>(Collections.singletonList(
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
        Cache cache = new Cache(null, 24 * 60 * 60 * 1000);
        assertNull(cache.get());
        // when
        ArrayList<Photo> photos = new ArrayList<>(Collections.singletonList(
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
        Cache cache = spy(new Cache(file, 24 * 60 * 60 * 1000));
        doNothing().when(cache).write();
        assertNull(cache.get());
        // when
        ArrayList<Photo> photos = new ArrayList<>(Collections.singletonList(
                new Photo("url1", "id1")
        ));
        cache.put(photos);
        // then value was put into cache
        assertEquals(photos, cache.get());
        // then write was call
        verify(cache, times(1)).write();
    }

    @Test
    public void cache_expired() throws IOException {
        // given an existing expired cache
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.lastModified()).thenReturn(System.currentTimeMillis() - (25 * 60 * 60 * 1000));
        Cache cache = spy(new Cache(file, 24 * 60 * 60 * 1000));

        // when
        ArrayList<Photo> actual = cache.get();

        // then check that read was not called and reset was called
        assertNull(actual);
        verify(cache, never()).read();
        verify(cache).reset();
    }

    @Test
    public void cache_not_expired() throws IOException {
        // given an existing expired cache
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.lastModified()).thenReturn(System.currentTimeMillis() - (60 * 1000));

        Cache cache = spy(new Cache(file, 24 * 60 * 60 * 1000));
        ArrayList<Photo> expected = new ArrayList<>(Collections.singletonList(
                new Photo("url1", "id1")
        ));
        doReturn(expected).when(cache).read();

        // when
        ArrayList<Photo> actual = cache.get();

        // then
        assertEquals(expected,actual);
        verify(cache).read();
        verify(cache, never()).reset();
    }
}
