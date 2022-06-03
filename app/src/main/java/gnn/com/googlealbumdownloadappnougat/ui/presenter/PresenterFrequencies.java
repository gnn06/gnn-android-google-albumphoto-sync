package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;
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

        activity.highlightStepWizard(true, WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY);
    }

    @Override
    public void onAppStop() {
        this.persist.saveFrequencies(getFrequencyWallpaper(), getFrequencySyncHour(), getFrequencyUpdatePhotos());
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
    public int getFrequencySyncHour() {
        return frequencySync;
    }

    /**
     *
     * @param frequency in hour
     */
    @Override
    public void setFrequencySyncHour(int frequency) {
        this.frequencySync = frequency;
        view.setFrequencySync(frequency);
    }

    /**
     *
     * int frequencySync fréquence de téléchargement en minute
     */
    @Override
    public int getFrequencySyncMinute() {
        if (getFrequencySyncHour() == -1)
            return Integer.MAX_VALUE;
        if (getFrequencySyncHour() < Integer.MAX_VALUE)
            return getFrequencySyncHour() * 60;
        else
            return Integer.MAX_VALUE;
    }

    /**
     *
     * @return frequence de recupération des nouvelles phtos in days
     */
    @Override
    public int getFrequencyUpdatePhotos() {
        return this.frequencyUpdate;
    }

    /**
     *
     * @param frequency in days
     */
    @Override
    public void setFrequencyUpdatePhotos(int frequency) {
        this.frequencyUpdate = frequency;
        view.setFrequencyUpdate(frequency);
    }

    /**
     * @return fréquence en minute
     */
    public int getFrequencyUpdatePhotosHour() {
        if (getFrequencyUpdatePhotos() == -1)
            return Integer.MAX_VALUE;
        if (getFrequencyUpdatePhotos() < Cache.DELAY_NEVER_EXPIRE)
            return getFrequencyUpdatePhotos() * 24;
        else
            return Cache.DELAY_NEVER_EXPIRE;
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
//            view.enableFrequencyWallpaper(checked);
//            view.enableFrequencySync(checked);
//            view.enableFrequencyUpdatePhotos(checked);
        }
    }

    @Override
    public void chooseFrequencyWallpaper() {
        if (scheduler.isScheduled()) {
            view.alertNeedDisableSchedule();
            return;
        }
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> setFrequencyWallpaper(value),
                R.array.frequency_wallpaper_value, R.array.frequency_wallpaper_label);
        dialogFrequency.show();
    }

    @Override
    public void chooseFrequencySync() {
        if (scheduler.isScheduled()) {
            view.alertNeedDisableSchedule();
            return;
        }
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> setFrequencySyncHour(value),
                R.array.frequency_sync_value, R.array.frequency_sync_label);
        dialogFrequency.show();
    }

    @Override
    public void chooseFrequencyUpdate() {
        if (scheduler.isScheduled()) {
            view.alertNeedDisableSchedule();
            return;
        }
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> setFrequencyUpdatePhotos(value),
                R.array.frequency_update_value, R.array.frequency_update_label);
        dialogFrequency.show();
    }


}
