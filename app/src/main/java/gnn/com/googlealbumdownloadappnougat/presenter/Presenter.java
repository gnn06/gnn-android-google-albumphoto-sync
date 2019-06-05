package gnn.com.googlealbumdownloadappnougat.presenter;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public class Presenter implements IPresenter{

    private final IView view;

    public Presenter(IView view) {
        this.view = view;
    }

    private String album;

    @Override
    public void onAlbumChoosen(String albumName) {
        // TODO: 05/06/2019 call service
        this.album = albumName;
        view.onAlbumChoosenResult(albumName);
    }

    @Override
    public String getAlbum() {
        return album;
    }
}

