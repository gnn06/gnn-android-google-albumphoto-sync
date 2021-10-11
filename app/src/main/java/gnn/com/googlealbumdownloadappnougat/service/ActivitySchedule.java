package gnn.com.googlealbumdownloadappnougat.service;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkInfo;

import gnn.com.googlealbumdownloadappnougat.R;

public class ActivitySchedule extends AppCompatActivity implements IScheduleView {

    private PresenterSchedule presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        presenter = new PresenterSchedule(this);
        presenter.onInit();
    }

    @Override
    public void setStateSync(WorkInfo state) {
        TextView view = findViewById(R.id.info);
        if (state != null) {
            view.setText(state.toString());
        } else {
            view.setText("no worker");
        }
    }

    @Override
    public void setStateWallpaper(WorkInfo state) {
        TextView view = findViewById(R.id.infoWallpaper);
        if (state != null) {
            view.setText(state.toString());
        } else {
            view.setText("no worker");
        }
    }

    public void onCancelSyncClick(View view) {
        presenter.onCancelSync();
    }

    public void onCancelWallpaperClick(View view) {
        presenter.onCancelWallpaper();
    }
}