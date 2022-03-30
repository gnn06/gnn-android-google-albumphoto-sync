package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

public interface IPresenterFrequencies {
    Context getContext();

    void onAppStart();

    int getFrequencyWallpaper();

    /**
     * @param frequency in minute
     */
    void setFrequencyWallpaper(int frequency);

    int getFrequencySyncHour();

    void setFrequencySyncHour(int frequency);

    int getFrequencyUpdatePhotos();

    void setFrequencyUpdatePhotos(int frequency);

    void onSwitchWallpaper(boolean checked);

    int getFrequencySyncMinute();

    void chooseFrequencyWallpaper();

    void chooseFrequencySync();

    void chooseFrequencyUpdate();

    void onAppStop();
}
