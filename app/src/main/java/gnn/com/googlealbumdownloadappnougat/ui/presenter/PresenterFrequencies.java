package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.photos.service.Cache;

public class PresenterFrequencies implements IPresenterFrequencies {

    private int frequencyMinute;
    private int frequencySync;
    private int frequencyUpdate;

    final private Context context;

    private final FragmentFrequencies fragment;
    private final PersistPrefMain persist;
    private final WallpaperScheduler scheduler;
    private final WallpaperSchedulerWithPermission scheduleWithPermission;
    private final ScheduleFromFreq schedulerFromFreq;

    public PresenterFrequencies(FragmentFrequencies fragment, Context context) {
        this.context = context;
        this.fragment = (FragmentFrequencies) fragment;
        this.persist = ServiceLocator.getInstance().getPersistMain();
        this.scheduler = ServiceLocator.getInstance().getWallpaperScheduler();
        this.scheduleWithPermission = ServiceLocator.getInstance().getSyncTask();
        this.schedulerFromFreq = new ScheduleFromFreq(this, this.scheduleWithPermission);
    }

    // For test
    public PresenterFrequencies(IViewFrequencies fragment, Context context,
                                PersistPrefMain persist,
                                WallpaperScheduler scheduler, WallpaperSchedulerWithPermission scheduleWithPermission, ScheduleFromFreq schedulerFromFreq) {
        this.context = context;
        this.fragment = (FragmentFrequencies) fragment;
        this.persist = persist;
        this.scheduler = scheduler;
        this.scheduleWithPermission = scheduleWithPermission;
        this.schedulerFromFreq = schedulerFromFreq;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onAppStart() {
        this.persist.restoreFrequencies(this);
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
     * called by Dialog
     * @param frequency in minute >= 15 minutes
     */
    @Override
    public void setFrequencyWallpaper(int frequency) {
        this.frequencyMinute = frequency;
        fragment.setFrequencyWallpaper(frequency);
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
        fragment.setFrequencySync(frequency);
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
        fragment.setFrequencyUpdate(frequency);
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
    public void chooseFrequencyWallpaper() {
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> {
                    setFrequencyWallpaper(value);
                    schedulerFromFreq.scheduleOrCancel();
                },
            R.array.frequency_wallpaper_value, R.array.frequency_wallpaper_label);
        dialogFrequency.show();
    }

    @Override
    public void chooseFrequencySync() {
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> {
                    setFrequencySyncHour(value);
                    schedulerFromFreq.scheduleOrCancel();
                },
            R.array.frequency_sync_value, R.array.frequency_sync_label);
        dialogFrequency.show();
    }

    @Override
    public void chooseFrequencyUpdate() {
        DialogFrequency dialogFrequency = new DialogFrequency(getContext(), value -> {
                    setFrequencyUpdatePhotos(value);
                    schedulerFromFreq.scheduleOrCancel();
                },
                R.array.frequency_update_value, R.array.frequency_update_label);
        dialogFrequency.show();
    }
}
