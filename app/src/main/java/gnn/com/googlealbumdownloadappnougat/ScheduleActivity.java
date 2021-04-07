package gnn.com.googlealbumdownloadappnougat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.Persistence;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.SyncData;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TAG = "ScheduleActivity";
    private final Scheduler scheduler = new Scheduler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        scheduler.dumpWorker();
    }

    public void scheduleOnclick(View view) {
        Scheduler sched = new Scheduler(this);
        SyncData data = new Persistence(null).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(this);
        sched.schedule(data.getAlbum(),
                data.getFolderHuman(),
                data.getRename(),
                data.getQuantity(),
                appContext);
    }

}