package gnn.com.googlealbumdownloadappnougat.presenter;

import android.content.Intent;

public interface IPresenter {

    void onSignIn();

    void onSignOut();

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void onSyncClick();

    void handlePermission(int i);

    String getFolderHuman();

    void chooseFolder();

    void chooseFolderResult(Intent data);
}
