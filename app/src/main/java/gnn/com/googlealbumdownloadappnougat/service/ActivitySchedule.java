package gnn.com.googlealbumdownloadappnougat.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.service.IScheduleView;
import gnn.com.googlealbumdownloadappnougat.service.PresenterSchedule;
import gnn.com.googlealbumdownloadappnougat.service.Scheduler;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistenceMain;

public class ActivitySchedule extends AppCompatActivity implements IScheduleView {

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
        TextView view = findViewById(R.id.textInterval);
        view.setText(Integer.toString(interval));
    }

    @Override
    public int getInterval() {
        TextView view = findViewById(R.id.textInterval);
        return Integer.parseInt(view.getText().toString());
    }
}