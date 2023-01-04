package gnn.com.googlealbumdownloadappnougat.wallpaper;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class PhotoScaleAndroidTest {

    @Test
    public void portrait_portrait() {
        // when
        float scale = PhotoScaleAndroid.getScale(150, 100, 100, 75);
        // then
        assertThat(scale, is(150f/100.0f));
    }

    @Test
    public void landscape_landscape() {
        // when
        float scale = PhotoScaleAndroid.getScale(100, 150, 75, 100);
        // then
        assertThat(scale, is(150f/100.0f));
    }

    @Test
    public void landscape_portait() {
        // when
        float scale = PhotoScaleAndroid.getScale(200, 100, 125, 150);
        // then
        assertThat(scale, is(200.0f/125.0f));
    }

    @Test
    public void portait_landscape() {
        // when
        float scale = PhotoScaleAndroid.getScale(100, 250, 150, 175);
        // then
        assertThat(scale, is(250.0f/175.0f));
    }
}