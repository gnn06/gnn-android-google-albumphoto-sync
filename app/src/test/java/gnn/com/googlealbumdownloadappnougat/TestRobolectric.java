package gnn.com.googlealbumdownloadappnougat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestRobolectric {
    @org.junit.Test
    public void test() {
//        ShadowBitmapFactory.provideWidthAndHeightHints("/home/gnn/Images/Papiers peints/IMG_20200614_174102.jpgTOTO", 100, 100);
        Bitmap bitmap = BitmapFactory.decodeFile("/home/gnn/Images/Papiers peints/IMG_20200614_174102.jpgTOTO");
        assertThat(bitmap, is(notNullValue()));
    }
}
