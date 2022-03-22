package gnn.com.googlealbumdownloadappnougat.ui.presenter;

public interface IPresenterFrequencies {
    void onAppStart();

    int getFrequencyWallpaper();

    /**
     * @param frequency in minute
     */
    void setFrequencyWallpaper(int frequency);

    int getFrequencySync();

    void setFrequencySync(int frequency);

    int getFrequencyUpdatePhotos();

    void setFrequencyUpdatePhotos(int frequency);

    void onSwitchWallpaper(boolean checked);

    int getFrequencySyncMinute();


    void chooseFrequency();
}
