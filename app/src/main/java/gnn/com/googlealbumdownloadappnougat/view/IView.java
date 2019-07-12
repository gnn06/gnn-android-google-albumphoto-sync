package gnn.com.googlealbumdownloadappnougat.view;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;

public interface IView {

    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onAlbumChoosenResult(String albumName);

    void setProgressBarVisibility(int visible);

    void alertNoAlbum();

    void updateUI_User();

    void updateUI_CallResult(String result);
}
