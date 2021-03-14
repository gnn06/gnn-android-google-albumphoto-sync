package gnn.com.photos.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import gnn.com.photos.Photo;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {

    @Before
    public void before() {
        Cache.getCache().reset();
    }

    @Test
    public void test_empty_no_persistence() throws IOException {
        // given an empty cache
        Cache cache = Cache.getCache();
        // when get cache
        Object result = cache.get();
        // then, check that get an answer
        Assert.assertNull(result);
    }

    @Test
    public void test_notEmpty_no_persistence() throws IOException {
        // given an not empty cache
        Cache.config(null, 24 * 60 * 60 * 1000);
        Cache cache = Cache.getCache();
        ArrayList<Photo> photos = new ArrayList<>(Collections.singletonList(
                new Photo("url1", "id1")
        ));
        cache.put(photos);
        // when get cache
        Object result = cache.get();
        // then, check that get an answer
        Assert.assertEquals(photos, result);
    }

    @Test
    public void test_put_noPersistence() throws IOException {
        // given empty cache
        Cache.config(null, 24 * 60 * 60 * 1000);
        Cache cache = Cache.getCache();
        Assert.assertNull(cache.get());
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
        File file = Mockito.mock(File.class);
        Cache.config(file, 24 * 60 * 60 * 1000);
        Cache cache = Mockito.spy(Cache.getCache());
        Mockito.doNothing().when(cache).write();
        Assert.assertNull(cache.get());
        // when
        ArrayList<Photo> photos = new ArrayList<>(Collections.singletonList(
                new Photo("url1", "id1")
        ));
        cache.put(photos);
        // then value was put into cache
        assertEquals(photos, cache.get());
        // then write was call
        Mockito.verify(cache, Mockito.times(1)).write();
    }

    @Test
    public void cache_expired() throws IOException {
        // given an existing expired cache
        File file = Mockito.mock(File.class);
        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.lastModified()).thenReturn(System.currentTimeMillis() - (25 * 60 * 60 * 1000));
        Cache.config(file, 24 * 60 * 60 * 1000);
        Cache cache = Mockito.spy(Cache.getCache());

        // when
        ArrayList<Photo> actual = cache.get();

        // then check that read was not called and reset was called
        Assert.assertNull(actual);
        Mockito.verify(cache, Mockito.never()).read();
        Mockito.verify(cache).reset();
    }

    @Test
    public void cache_not_expired() throws IOException {
        // given an existing expired cache
        File file = Mockito.mock(File.class);
        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.lastModified()).thenReturn(System.currentTimeMillis() - (60 * 1000));

        Cache.config(file, 24 * 60 * 60 * 1000);
        Cache cache = Mockito.spy(Cache.getCache());
        ArrayList<Photo> expected = new ArrayList<>(Collections.singletonList(
                new Photo("url1", "id1")
        ));
        Mockito.doReturn(expected).when(cache).read();

        // when
        ArrayList<Photo> actual = cache.get();

        // then
        Assert.assertEquals(expected,actual);
        Mockito.verify(cache).read();
        Mockito.verify(cache, Mockito.never()).reset();
    }
}
