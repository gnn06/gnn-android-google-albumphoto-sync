package gnn.com.googlealbumdownloadappnougat.presenter;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.remote.PhotosRemoteService;

public class GetAlbumsTask extends PhotosAsyncTask<Void, Void, ArrayList<String>> {

    private PhotosRemoteService prs;

    GetAlbumsTask(IPresenter presenter, PhotosRemoteService prs) {
        super(presenter);
        this.prs = prs;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> albumNames = null;
        try {
            albumNames = prs.getAlbums();
        } catch (IOException | GoogleAuthException e) {
            Log.e(TAG, e.getMessage());
            markAsError();
        }
        return albumNames;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final ArrayList<String> albums) {
        super.onPostExecute(albums);
        if (isSuccessful()) {
            presenter.setAlbums(albums);
        }
    }
}
