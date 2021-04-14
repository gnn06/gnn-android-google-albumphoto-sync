package gnn.com.googlealbumdownloadappnougat.service;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistenceMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.SyncData;

public class PresenterSchedule implements IPresenterSchedule {

    private static final String TAG = "Schedule";
    public static int DefaultInterval = 24;

    private final IScheduleView view;
    private final ActivitySchedule activity;

    public PresenterSchedule(ActivitySchedule activity)
    {
        this.activity = activity;
        this.view = activity;
    }

    @Override
    public void onInit() {
        new PersistenceSchedule(this.activity).restore(this);
    }

    @Override
    public void onSchedule() {
        Scheduler sched = new Scheduler(activity);
        SyncData data = new PersistenceMain(activity).getData();
        ApplicationContext appContext = ApplicationContext.getInstance(activity);
        int interval = view.getInterval();
        sched.schedule(data.getAlbum(),
                data.getFolderHuman(),
                data.getRename(),
                data.getQuantity(),
                interval,
                appContext);
    }

    @Override
    public void cancel() {
        Scheduler sched = new Scheduler(activity);
        sched.cancel();
    }

    @Override
    public int getInterval() {
        return view.getInterval();
    }

    @Override
    public void setInterval(int interval) {
        view.setInterval(interval);
    }
}

