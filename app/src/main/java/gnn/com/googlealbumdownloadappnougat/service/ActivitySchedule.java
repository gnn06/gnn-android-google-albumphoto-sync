package gnn.com.googlealbumdownloadappnougat.service;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkInfo;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.R;

public class ActivitySchedule extends AppCompatActivity implements IScheduleView {

    private static final String TAG = "ScheduleActivity";

    private PresenterSchedule presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        presenter = new PresenterSchedule(this);
        presenter.onInit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        new PersistenceSchedule(this).save(presenter);
        // not necessary to save settings as settings can not be changed in this activity
    }

    public void onScheduleClick(View view) {
        presenter.onSchedule();
    }

    public void onCancelClick(View view) {
        presenter.cancel();
    }

    public void onWallpaperClick(View view) {
        String path = "/sdcard/Pictures/ADoMfeQj6d-sExfOjnrmN0QHHGRERVUz4Id9o4QmChvwZSqHTZgEnn4QbZkKkaqbq8ym-5zOaY4nOsUQefGenJAGHe9y5CTBUQ.jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1080, 1920, true);
            wallpaperManager.setBitmap(scaledBitmap);
        } catch (IOException e) {
            Log.e("SCHEDULE", e.getMessage());
        }
    }

    @Override
    public void setInterval(int interval) {
        TextView view = findViewById(R.id.textInterval);
        view.setText(Integer.toString(interval));
    }

    @Override
    public int getInterval() {
        TextView view = findViewById(R.id.textInterval);
        return Integer.parseInt(view.getText().toString());
    }

    @Override
    public void setState(WorkInfo state) {
        TextView view = findViewById(R.id.info);
        if (state != null) {
            view.setText(state.toString());
        } else {
            view.setText("no worker");
        }
    }
}