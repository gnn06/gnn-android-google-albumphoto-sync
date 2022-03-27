package gnn.com.googlealbumdownloadappnougat.ui.view;

public interface IViewFrequencies extends IView {

    void setSwitchWallpaper(boolean scheduled);

    void setFrequencyWallpaper(int frequency);

//    void enableFrequencyWallpaper(boolean switchChecked);

    void setFrequencySync(int frequency);

    void setFrequencyUpdate(int frequency);

//    void enableFrequencySync(boolean switchChecked);

//    void enableFrequencyUpdatePhotos(boolean switchChecked);

    void alertFrequencyError();

    void alertNeedDisableSchedule();
}
