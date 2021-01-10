package gnn.com.googlealbumdownloadappnougat.view;

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

    void updateUI_Folder(String humanPath);

    void showError();
}
