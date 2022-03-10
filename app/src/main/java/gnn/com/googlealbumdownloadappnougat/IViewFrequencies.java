package gnn.com.googlealbumdownloadappnougat;

public interface IViewFrequencies {

    void setSwitchWallpaper(boolean scheduled);

    String getFrequencyWallpaper();

    void setFrequencyWallpaper(String frequency);

    void enableFrequencyWallpaper(boolean switchChecked);

    String getFrequencySync();

    void setFrequencySync(String frequency);

    void enableFrequencySync(boolean switchChecked);

    String getFrequencyUpdatePhotos();

    void setFrequencyUpdatePhotos(String frequency);

    void enableFrequencyUpdatePhotos(boolean switchChecked);

    void alertFrequencyError();
}
