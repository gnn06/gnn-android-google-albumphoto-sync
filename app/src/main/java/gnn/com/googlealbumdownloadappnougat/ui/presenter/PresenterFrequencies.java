package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.content.Context;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.SynchronizerDelayed;

public class PresenterFrequencies implements IPresenterFrequencies {

    private int frequencyMinute;
    private int frequencySync;
    private int frequencyUpdate;

    final private Context context;

    private final FragmentFrequencies fragment;
    private final PersistPrefMain persist;
    private final ScheduleFromFreq schedulerFromFreq;

    public PresenterFrequencies(FragmentFrequencies fragment, Context context) {
        this.context = context;
        this.fragment = fragment;
        this.persist = ServiceLocator.getInstance().getPersistMain();
        this.schedulerFromFreq = new ScheduleFromFreq(this);
    }

    // For test
    public PresenterFrequencies(IViewFrequencies fragment, Context context,
                                PersistPrefMain persist,
                                ScheduleFromFreq schedulerFromFreq) {
        this.context = context;
        this.fragment = (FragmentFrequencies) fragment;
        this.persist = persist;
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
        if (getFrequencySyncHour() == Frequency.NEVER)
            return SynchronizerDelayed.DELAY_NEVER_SYNC;
        else if (getFrequencySyncHour() == 0) {
            return SynchronizerDelayed.DELAY_ALWAYS_SYNC;
        } else if (getFrequencySyncHour() < Integer.MAX_VALUE)
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
        if (getFrequencyUpdatePhotos() == Frequency.NEVER)
            return Cache.DELAY_NEVER_EXPIRE;
        else if (getFrequencyUpdatePhotos() == 0) {
            return Cache.DELAY_ALWAYS_EXPIRE;
        } else if (getFrequencyUpdatePhotos() < Integer.MAX_VALUE)
            return getFrequencyUpdatePhotos() * 24;
        else
            return Integer.MAX_VALUE;
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

    @Override
    public void explanation(int idText_Title, int idText_Message) {
        new AlertDialog.Builder(fragment.getActivity())
                .setTitle(getContext().getResources().getString(R.string.title_frequency_explanation))
                .setMessage(getContext().getResources().getString(idText_Message))
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }
}
