package gnn.com.googlealbumdownloadappnougat.photos;

import android.graphics.Rect;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.wallpaper.PhotoScaleAndroid;

class PhotoScaleTest {

    @Test
    public void scale1() {
        int photoWidth = 512;
        int photoHeight = 370;
        int displayWidth = 1080;
        int displayHeight = 1920;
        int scaleWidth = 1080;
        int scaleHeight = 370 * 1080 / 1920; // 208.125
        Rect rect = PhotoScaleAndroid.scale(photoWidth, photoHeight, displayWidth, displayHeight);
    }
}
