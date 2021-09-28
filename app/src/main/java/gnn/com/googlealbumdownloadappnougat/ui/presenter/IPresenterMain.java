package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public interface IPresenterMain {

    String getAlbum();

    void setAlbum(String album);

    void onSignIn();

    void onSignOut();

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void setAlbums(ArrayList<String> mAlbums);

    void onSyncClick();

    void onChooseSync();

    void handlePermission(int i);

    String getFolderHuman();

    File getFolder();

    void setFolderHuman(String folderHuman);

    void chooseFolder();

    void setFolder(Intent data);

    void showError(String errorMessage);

    void setProgressBarVisibility(int visible);

    void setSyncResult(SynchronizerAndroid sync, SyncStep starting);

    void onResetCache();

    void init();

    int getQuantity();

    void setQuantity(int quantity);

    String getRename();

    void setRename(String rename);

    void onButtonScheduleDetail();

    void onButtonWallpaper();

    int getFrequencyWallpaper();

    void setFrequencyWallpaper(int frequency);

    void onButtonSchedule();
}

