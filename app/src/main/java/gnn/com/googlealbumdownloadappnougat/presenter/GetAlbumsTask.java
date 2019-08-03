package gnn.com.googlealbumdownloadappnougat.presenter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.remote.PhotosRemoteService;

// TODO: 30/07/2019 mutualise code pour récupérer GoogleClient
public class GetAlbumsTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private static final String TAG = "goi";

    GetAlbumsTask(IPresenter presenter, PhotosRemoteService prs) {
        this.presenter = presenter;
        this.prs = prs;
    }

    private PhotosRemoteService prs;
    private IPresenter presenter;

    boolean error = false;

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        // TODO: 30/07/2019 manage exceptions
        ArrayList<String> albumNames = null;
        try {
            albumNames = prs.getAlbums();
        } catch (IOException | GoogleAuthException e) {
            Log.e(TAG, e.getMessage());
            this.error = true;
        }
        return albumNames;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        presenter.setProgressBarVisibility(ProgressBar.VISIBLE);
        this.error = false;
    }

    @Override
    protected void onPostExecute(final ArrayList<String> albums) {
        super.onPostExecute(albums);
        presenter.setProgressBarVisibility(ProgressBar.INVISIBLE);
        if (error) {
            presenter.showError();
        } else {
            presenter.setAlbums(albums);
        }
    }
}
