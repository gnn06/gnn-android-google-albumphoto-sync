package gnn.com.googlealbumdownloadappnougat.presenter;

import java.io.File;

public interface IPresenter {

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void handleSignInResult();

    void onSyncClick();

    void laucnhSync();

    void launchSynchWithPermission();

    File getFolder();
}
