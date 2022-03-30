package gnn.com.googlealbumdownloadappnougat.ui.view;

import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.photos.stat.stat.WallpaperStat;

public interface IViewHome extends IView {

    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onAlbumChosenResult(String albumName);

    void setProgressBarVisibility(int visible);

    void alertNoAlbum();

    void updateUI_User();

    void updateUI_CallResult(SynchronizerAndroid synchronizer, SyncStep step);

    void updateUI_lastSyncTime(Date lastUpdateTime, Date lastSyncTime, Date lastWallpaperTime);

    void updateUI_Folder(String humanPath);

    void setStat(WallpaperStat stat);

    void setWarningWallpaperActive(boolean active);
}
