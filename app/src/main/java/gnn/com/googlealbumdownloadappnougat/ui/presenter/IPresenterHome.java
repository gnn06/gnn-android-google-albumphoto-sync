package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public interface IPresenterHome {

    String getAlbum();

    void setAlbum(String album);

    void onSignIn();

    void onSignOut();

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void setAlbums(ArrayList<String> mAlbums);

    String getFolderHuman();

    File getFolder();

    void setFolderHuman(String folderHuman);

    void onChooseFolder();

    void setFolder(Intent data);

    void showError(String errorMessage);

    void setProgressBarVisibility(int visible);

    void setSyncResult(SynchronizerAndroid sync, SyncStep starting);

    void onAppStart();

    void refreshLastTime();

    void onButtonSyncOnce();

    void onButtonWallpaperOnce();

    void onMenuResetCache();

    void onMenuScheduleDetail();

    void onMenuRequestGooglePermission();

    void onAppForeground();

    void onWarningWallpaperActive();
}

