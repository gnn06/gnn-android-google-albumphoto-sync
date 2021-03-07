package gnn.com.googlealbumdownloadappnougat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TAG = "ScheduleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        // "e248680d-778f-41b8-9992-79da52fb8d83"
        try {
            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            ListenableFuture<List<WorkInfo>> infos = workManager.getWorkInfosByTag("gnn.com.googlealbumdownloadappnougat.MyWorker");
            Log.i(TAG, "getWorkInfosByTag.size= " + infos.get().size());
            for (Iterator<WorkInfo> it = infos.get().iterator(); it.hasNext(); ) {
                WorkInfo info = it.next();
                Log.i(TAG, String.valueOf(info.getState().isFinished()));
            }
//            workManager.cancelAllWorkByTag("gnn.com.googlealbumdownloadappnougat.MyWorker");
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "onCreate: worker statut", e);
        }
    }

    public void scheduleOnclick(View view) {
        PeriodicWorkRequest work = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork("mywork", ExistingPeriodicWorkPolicy.KEEP, work);
    }
}