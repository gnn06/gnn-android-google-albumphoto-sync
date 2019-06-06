package gnn.com.googlealbumdownloadappnougat.presenter;

import java.io.File;

public interface IPresenter {

    void onAlbumChoose();

    void launchSynchWithPermission();

    void onAlbumChoosen(String albumName);

    String getAlbum();

    void onSyncClick();
    void laucnhSync();

    File getFolder();
}
