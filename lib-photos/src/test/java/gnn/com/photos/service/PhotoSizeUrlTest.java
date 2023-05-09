package gnn.com.photos.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import gnn.com.util.ScreenSize;

public class PhotoSizeUrlTest {

    PhotoSizeUrl UT;

    @Before
    public void setUp() throws Exception {
        UT = new PhotoSizeUrl();
    }

    @Test
    public void normal_portrait() throws MalformedURLException {
        // when
        URL result = UT.getUrl(new URL("http://baseUrl"), new ScreenSize(200, 400));
        assertEquals("http://baseUrl=w400-h400", result.toString());
    }

    @Test
    public void normal_landscape() throws MalformedURLException {
        // when
        URL result = UT.getUrl(new URL("http://baseUrl"), new ScreenSize(400, 200));
        assertEquals("http://baseUrl=w400-h400", result.toString());
    }

    @Test
    public void noSize () throws MalformedURLException {
        // when
        URL result = UT.getUrl(new URL("http://baseUrl"), new ScreenSize(0, 0));
        assertEquals("http://baseUrl", result.toString());
    }
}