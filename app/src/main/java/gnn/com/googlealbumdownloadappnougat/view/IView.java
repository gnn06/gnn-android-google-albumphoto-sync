package gnn.com.googlealbumdownloadappnougat.view;

import java.util.ArrayList;

public interface IView {

    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onAlbumChoosenResult(String albumName);

    void setChooseAlbumProgressBarVisibility(int visible);

    void setSyncProgresBarVisibility(int visible);

    void updateUI_User();

    void updateUI_CallResult(String result);
}
