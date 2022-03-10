package gnn.com.googlealbumdownloadappnougat.ui.view;

import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public interface IViewFrequencies extends IView {

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
