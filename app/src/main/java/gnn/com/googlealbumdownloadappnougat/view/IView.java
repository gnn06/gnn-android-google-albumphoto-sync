package gnn.com.googlealbumdownloadappnougat.view;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.photos.sync.Synchronizer;

public interface IView {

    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onAlbumChoosenResult(String albumName);

    void setProgressBarVisibility(int visible);

    void alertNoAlbum();

    void updateUI_User();

    void updateUI_CallResult(Synchronizer synchronizer, SyncStep step);

    void updateUI_Folder(String humanPath);
}
