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

    public void onScheduleClick(View view) {
        presenter.onSchedule();
    }

    public void onCancelClick(View view) {
        presenter.cancel();
    }

    @Override
    public void setInterval(int interval) {
        TextView view = findViewById(R.id.textIntervalSync);
        view.setText(Integer.toString(interval));
    }

    @Override
    public int getInterval() {
        TextView view = findViewById(R.id.textIntervalSync);
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