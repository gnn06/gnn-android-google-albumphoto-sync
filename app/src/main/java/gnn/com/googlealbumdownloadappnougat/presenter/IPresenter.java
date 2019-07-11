package gnn.com.googlealbumdownloadappnougat.presenter;

import java.io.File;

public interface IPresenter {

    void onSignIn();

    void onSignOut();

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void onSyncClick();

    void handlePermission(int i);

    File getFolder();
}
