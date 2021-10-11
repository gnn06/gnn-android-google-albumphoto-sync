package gnn.com.googlealbumdownloadappnougat.photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import gnn.com.googlealbumdownloadappnougat.wallpaper.PhotoScaleAndroid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

//@RunWith(RobolectricTestRunner.class)
@RunWith(AndroidJUnit4.class)
@Ignore
public class PhotoScaleAndroidTest {
    @Test
    public void test() throws FileNotFoundException {
//        Bitmap bitmap = BitmapFactory.decodeFile("/home/gnn/Images/Papiers peints/IMG_20200614_174102.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Pictures/template.jpg");
        assertThat(bitmap, is(notNullValue()));

        Bitmap scaledBitmap  = PhotoScaleAndroid.scale(bitmap, 1080, 1920);

        OutputStream stream = new FileOutputStream("/sdcard/Pictures/toto.jpg");
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream);
    }
}
