package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

public class PhotoScaleAndroid {

    static public Bitmap scale(Bitmap bitmap, int displayWidth, int displayHeight) {

        // TODO BG_Bitmap est inutile en mode maximize où il n'y a jamais de background à remplir
        Bitmap BG_Bitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(BG_Bitmap);

        Matrix transformation = getMatrix(BG_Bitmap, displayWidth, displayHeight);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(bitmap, transformation, paint);
        return BG_Bitmap;
    }

    @NonNull
    static Matrix getMatrix(Bitmap bitmap, int displayWidth, int displayHeight) {
        Matrix matrix = new Matrix();

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

        matrix.postScale(scale, scale);
        matrix.postTranslate(translateX, translateY);

        return matrix;
    }

    public static Rect scale(int photoWidth, int photoHeight, int displayWidth, int displayHeight) {
        int scaleWidth = 0;
        int scaleHeight = 0;
        if (photoWidth > photoHeight) {
            scaleWidth = photoHeight * displayWidth / displayHeight;
            scaleHeight = photoHeight;
        } else {

        }
        return new Rect();
    }
}
