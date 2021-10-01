package gnn.com.googlealbumdownloadappnougat.ui.view;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public interface IView {

    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onAlbumChosenResult(String albumName);

    void setProgressBarVisibility(int visible);

    void alertNoAlbum();

    void updateUI_User();

    void updateUI_CallResult(SynchronizerAndroid synchronizer, SyncStep step);

    void updateUI_lastSyncTime(String lastSyncTime);

    void updateUI_Folder(String humanPath);

    void showError(String message);

    String getQuantity();

    void setQuantity(String quantity);

    String getRename();

    void setRename(String rename);

    String getFrequencyWallpaper();

    void setFrequencyWallpaper(String frequency);

    void setSwitchWallpaper(boolean scheduled);
}
