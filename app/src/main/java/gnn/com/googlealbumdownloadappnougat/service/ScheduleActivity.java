package gnn.com.googlealbumdownloadappnougat.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.service.IScheduleView;
import gnn.com.googlealbumdownloadappnougat.service.PresenterSchedule;
import gnn.com.googlealbumdownloadappnougat.service.Scheduler;

public class ScheduleActivity extends AppCompatActivity implements IScheduleView {

    private static final String TAG = "ScheduleActivity";

    private final Scheduler scheduler = new Scheduler(this);

    private PresenterSchedule presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        presenter = new PresenterSchedule(this);
        presenter.onInit();
        // TODO get last schedule state
    }

    public void onScheduleClick(View view) {
        presenter.onSchedule();
    }

    public void onCancelClick(View view) {
        presenter.cancel();
    }

    @Override
    public void setInterval(int interval) {
        TextView view = findViewById(R.id.textInterval);
        view.setText(interval);
    }
}