package gnn.com.googlealbumdownloadappnougat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import gnn.com.googlealbumdownloadappnougat.service.Scheduler;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.Persistence;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.SyncData;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TAG = "ScheduleActivity";
    private final Scheduler scheduler = new Scheduler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        // TODO get last schedule state
    }

    public void scheduleOnclick(View view) {
        Scheduler sched = new Scheduler(this);
        SyncData data = new Persistence(this).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(this);
        sched.schedule(data.getAlbum(),
                data.getFolderHuman(),
                data.getRename(),
                data.getQuantity(),
                24,
                appContext);
    }

    public void cancelOnclick(View view) {
        Scheduler sched = new Scheduler(this);
        sched.cancel();
    }
}