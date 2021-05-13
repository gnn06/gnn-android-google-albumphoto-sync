package gnn.com.googlealbumdownloadappnougat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowBitmapFactory;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@RunWith(RobolectricTestRunner.class)
public class TestRobolectric {
    @org.junit.Test
    public void test() {
//        ShadowBitmapFactory.provideWidthAndHeightHints("/home/gnn/Images/Papiers peints/IMG_20200614_174102.jpgTOTO", 100, 100);
        Bitmap bitmap = BitmapFactory.decodeFile("/home/gnn/Images/Papiers peints/IMG_20200614_174102.jpgTOTO");
        assertThat(bitmap, is(notNullValue()));
    }
}
