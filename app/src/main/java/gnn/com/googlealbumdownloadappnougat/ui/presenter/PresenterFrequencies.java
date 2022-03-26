package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.photos.service.Cache;

public class PresenterFrequencies implements IPresenterFrequencies {

    private int frequencyMinute;
    private int frequencySync;
    private int frequencyUpdate;

    final private IViewFrequencies view;
    final private Context context;
    private ScheduleTask task;

    @Override
    public Context getContext() {
        return context;
    }

    private MainActivity activity;
    private final UserModel userModel;
    private final PersistPrefMain persist;
    private final WallpaperScheduler scheduler;

    public PresenterFrequencies(IViewFrequencies view, Context context, MainActivity activity) {
        this.view = view;
        this.context = context;
        this.activity = activity;
        this.userModel = new ViewModelProvider(activity).get(UserModel.class);
        this.persist = new PersistPrefMain(context);
        this.scheduler = new WallpaperScheduler(context);
        task = new ScheduleTask(activity, context, scheduler, view, userModel);
    }

    // For test
    public PresenterFrequencies(IViewFrequencies view, Context context, MainActivity activity,
                                UserModel userModel, PersistPrefMain persist,
                                WallpaperScheduler scheduler, ScheduleTask scheduleTask) {
        this.view = view;
        this.context = context;
        this.activity = activity;
        this.userModel = userModel;
        this.persist = persist;
        this.scheduler = scheduler;
        this.task = scheduleTask;
    }

    @Override
    public void onAppStart() {
        this.persist.restoreFrequencies(this);

        boolean scheduled = this.scheduler.isScheduled();
        view.setSwitchWallpaper(scheduled);
        view.enableFrequencyWallpaper(scheduled);
        view.enableFrequencySync(scheduled);
        view.enableFrequencyUpdatePhotos(scheduled);
    }

    @Override
    public void onAppStop() {
        this.persist.saveFrequencies(getFrequencyWallpaper(), getFrequencySync(), getFrequencyUpdatePhotos());
    }

    /**
     *
     * @return in minute
     */
    @Override
    public int getFrequencyWallpaper() {
        return this.frequencyMinute;
    }

    /**
     * @param frequency in minute >= 15 minutes
     */
    @Override
    public void setFrequencyWallpaper(int frequency) {
        this.frequencyMinute = frequency;
        view.setFrequencyWallpaper(frequency);
    }

    /**
     *
     * @return int frequencySync fréquence de téléchargement en heure
     */
    @Override
    public int getFrequencySync() {
        return frequencySync;
    }

    /**
     *
     * @param frequency in hour
     */
    @Override
    public void setFrequencySync(int frequency) {
        this.frequencySync = frequency;
        view.setFrequencySync(frequency);
    }

    /**
     *
     * int frequencySync fréquence de téléchargement en minute
     */
    @Override
    public int getFrequencySyncMinute() {
        return getFrequencySync() < Integer.MAX_VALUE ? getFrequencySync() * 60 : Integer.MAX_VALUE;
    }

    /**
     *
     * @return frequence de recupération des nouvelles phtos in days
     */
    @Override
    public int getFrequencyUpdatePhotos() {
        String frequency = view.getFrequencyUpdatePhotos();
        if ("0".equals(frequency)) {
            return Cache.DELAY_ALWAYS_EXPIRE;
        } else if ("".equals(frequency)) {
            return Cache.DELAY_NEVER_EXPIRE;
        } else {
            return Integer.parseInt(frequency);
        }
    }

    /**
     *
     * @param frequency in days
     */
    @Override
    public void setFrequencyUpdatePhotos(int frequency) {
        view.setFrequencyUpdatePhotos(frequency == Cache.DELAY_NEVER_EXPIRE ? "" : Integer.toString(frequency));
    }

    /**
     * @return fréquence en minute
     */
    public int getFrequencyUpdatePhotosHour() {
        return getFrequencyUpdatePhotos() < Cache.DELAY_NEVER_EXPIRE ?
                    getFrequencyUpdatePhotos() * 24 : Cache.DELAY_NEVER_EXPIRE;
    }

    @Override
    public void onSwitchWallpaper(boolean checked) {
        if (checked) {
            if (getFrequencyWallpaper() < 15) {
                view.setSwitchWallpaper(false);
                view.alertFrequencyError();
            } else {
                // TODO manage permission refused and toggle switch off
                task.schedule(checked, getFrequencyWallpaper(), getFrequencySyncMinute(), getFrequencyUpdatePhotosHour());
            }
        } else {
            scheduler.cancel();
            view.enableFrequencyWallpaper(checked);
            view.enableFrequencySync(checked);
            view.enableFrequencyUpdatePhotos(checked);
        }
    }

    @Override
    public void chooseFrequency() {
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> setFrequencyWallpaper(value),
                R.array.frequency_wallpaper_value, R.array.frequency_wallpaper_label);
        dialogFrequency.show();
    }


}
