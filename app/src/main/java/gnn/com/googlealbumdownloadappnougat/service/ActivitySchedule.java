package gnn.com.googlealbumdownloadappnougat.service;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkInfo;

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

    @Override
    public void setState(WorkInfo state) {
        TextView view = findViewById(R.id.info);
        if (state != null) {
            view.setText(state.toString());
        } else {
            view.setText("no worker");
        }
    }

    public void onScheduleSyncClick(View view) {
        presenter.onScheduleSync();
    }

    public void onCancelSyncClick(View view) {
        presenter.onCancelSync();
    }

    @Override
    public void setIntervalSync(int interval) {
        TextView view = findViewById(R.id.textIntervalSync);
        view.setText(Integer.toString(interval));
    }

    @Override
    public int getIntervalSync() {
        TextView view = findViewById(R.id.textIntervalSync);
        return Integer.parseInt(view.getText().toString());
    }

    public void onScheduleWallpaperClick(View view) {
        presenter.onScheduleWallpaper();
    }

    public void onCancelWallpaperClick(View view) {
        presenter.onCancelWallpaper();
    }
}