package gnn.com.googlealbumdownloadappnougat.view;

import java.util.ArrayList;

public interface IView {

    void onAlbumChoosenResult(String albumName);
    void showChooseAlbumDialog(final ArrayList<String> mAlbums);

    void onGetAlbumsProgressBar(int visible);
}
