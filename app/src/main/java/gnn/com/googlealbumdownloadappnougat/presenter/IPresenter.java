package gnn.com.googlealbumdownloadappnougat.presenter;

import android.content.Intent;

import java.util.ArrayList;

public interface IPresenter {

    String getAlbum();

    void setAlbum(String album);

    void onSignIn();

    void onSignOut();

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void setAlbums(ArrayList<String> mAlbums);

    void onSyncClick();

    void handlePermission(int i);

    String getFolderHuman();

    void setFolderHuman(String folderHuman);

    void chooseFolder();

    void chooseFolderResult(Intent data);

    void showError();

    void setProgressBarVisibility(int visible);
}
