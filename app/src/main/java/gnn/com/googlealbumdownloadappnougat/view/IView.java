package gnn.com.googlealbumdownloadappnougat.view;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;

public interface IView {

    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onAlbumChoosenResult(String albumName);

    void setChooseAlbumProgressBarVisibility(int visible);

    void alertNoAlbum();

    void setSyncProgresBarVisibility(int visible);

    void updateUI_User();

    void updateUI_CallResult(String result);
}
