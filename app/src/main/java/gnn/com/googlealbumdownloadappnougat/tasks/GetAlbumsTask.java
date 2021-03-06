package gnn.com.googlealbumdownloadappnougat.tasks;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;
import gnn.com.photos.service.PhotosRemoteService;

public class GetAlbumsTask extends PhotosAsyncTask<Void, Void, ArrayList<String>> {

    private PhotosRemoteService prs;

    public GetAlbumsTask(IPresenterMain presenter, PhotosRemoteService prs) {
        super(presenter);
        this.prs = prs;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> albumNames = null;
        try {
            albumNames = prs.getAlbums();
        } catch (IOException | GoogleAuthException e) {
            Log.e(TAG, e.toString());
            markAsError(e.toString());
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
