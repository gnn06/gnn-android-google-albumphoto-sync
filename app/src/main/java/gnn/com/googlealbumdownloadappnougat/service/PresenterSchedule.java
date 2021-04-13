package gnn.com.googlealbumdownloadappnougat.service;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistenceMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.SyncData;

public class PresenterSchedule implements IPresenterSchedule {

    private static final String TAG = "Schedule";

    private final IScheduleView view;
    private final ScheduleActivity activity;

    public PresenterSchedule(ScheduleActivity activity)
    {
        this.activity = activity;
        this.view = activity;
    }

    @Override
    public void onInit() {
        view.setInterval(24);
    }

    @Override
    public void onSchedule() {
        Scheduler sched = new Scheduler(activity);
        SyncData data = new PersistenceMain(activity).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(activity);
        sched.schedule(data.getAlbum(),
                data.getFolderHuman(),
                data.getRename(),
                data.getQuantity(),
                24,
                appContext);
    }

    @Override
    public void cancel() {
        Scheduler sched = new Scheduler(activity);
        sched.cancel();
    }
}

