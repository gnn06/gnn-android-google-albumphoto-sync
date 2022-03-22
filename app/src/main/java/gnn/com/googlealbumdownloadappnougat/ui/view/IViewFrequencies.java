package gnn.com.googlealbumdownloadappnougat.ui.view;

public interface IViewFrequencies extends IView {

    void setSwitchWallpaper(boolean scheduled);

    void setFrequencyWallpaper(int frequency);

    void enableFrequencyWallpaper(boolean switchChecked);

    String getFrequencySync();

    void setFrequencySync(String frequency);

    void enableFrequencySync(boolean switchChecked);

    String getFrequencyUpdatePhotos();

    void setFrequencyUpdatePhotos(String frequency);

    void enableFrequencyUpdatePhotos(boolean switchChecked);

    void alertFrequencyError();
}
