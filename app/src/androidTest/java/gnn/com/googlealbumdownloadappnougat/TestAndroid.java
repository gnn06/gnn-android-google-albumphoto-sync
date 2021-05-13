package gnn.com.googlealbumdownloadappnougat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

//@RunWith(RobolectricTestRunner.class)
@RunWith(AndroidJUnit4.class)
public class TestAndroid {
    @Test
    public void test() throws FileNotFoundException {
//        Bitmap bitmap = BitmapFactory.decodeFile("/home/gnn/Images/Papiers peints/IMG_20200614_174102.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Pictures/template.jpg");
        assertThat(bitmap, is(notNullValue()));

        int displayWidth = 512;
        int displayHeight = 382;

        Bitmap BG_Bitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(BG_Bitmap);

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scale;
        int translateX, translateY;

        // width / height < 1 = width < height
        // ratio1 < ratio2 = width1 < width2
        if (originalWidth / originalHeight < displayWidth / displayHeight) {
            // photo à resizer par la largeur, la largeur aux max, la hauteur suit
            scale = (float)displayWidth / originalWidth;
        } else {
            // photo à resizer par la hauteur, la hauteur au max, la largeur suit
            scale = (float)displayHeight  / originalHeight;
        }
        translateY = (int)((displayHeight - (originalHeight * scale)) / 2);
        translateX = (int)((displayWidth - (originalWidth * scale)) / 2);

        Matrix transformation = new Matrix();
        transformation.postScale(scale, scale);
        transformation.postTranslate(translateX, translateY);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);


        canvas.drawBitmap(bitmap, transformation, paint);

        OutputStream stream = new FileOutputStream("/sdcard/Pictures/toto.jpg");
        BG_Bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream);
    }
}
