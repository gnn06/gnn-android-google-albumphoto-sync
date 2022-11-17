package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.photos.sync.Temp;

public interface IPresenterHome {

    String getAlbum();

    void setAlbum(String album);

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void setAlbums(ArrayList<String> mAlbums);

    String getDefaultFolderHuman();

    File getFolder();

    void setDefaultFolderHuman(String defaultFolderHuman);

    void onChooseFolder();

    void setFolder(Intent data);

    void showError(String errorMessage);

    void setProgressBarVisibility(int visible);

    void setSyncResult(Temp sync, SyncStep starting);

    void onAppStart();

    void onButtonSyncOnce();

    void onButtonWallpaperOnce();

    void onAppForeground();

    void onWarningWallpaperActive();

    void refreshLastTime();

    void onSignIn();
}

