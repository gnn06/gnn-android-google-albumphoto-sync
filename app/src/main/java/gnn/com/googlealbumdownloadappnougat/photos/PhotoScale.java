package gnn.com.googlealbumdownloadappnougat.photos;

import android.graphics.Rect;

class PhotoScale {

    static Rect scale(int photoWidth, int photoHeight, int displayWidth, int displayHeight) {
        int scaleWidth = 0;
        int scaleHeight = 0;
        if (photoWidth > photoHeight) {
            scaleWidth = photoHeight * displayWidth / displayHeight;
            scaleHeight = photoHeight;
        } else {

        }
        return new Rect()
        return null;
    }
}
