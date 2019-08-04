package gnn.com.googlealbumdownloadappnougat.presenter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.photos.sync.Synchronizer;

public class SyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "goi";

    public SyncTask(IPresenter presenter, Synchronizer sync) {
        this.presenter = presenter;
        this.sync = sync;
    }

    private IPresenter presenter;

    private Synchronizer sync;

    public boolean error;

    @Override
    protected Void doInBackground(Void... voids) {
        // TODO: 30/07/2019 manage exceptions
        String album = presenter.getAlbum();
        File destination = presenter.getFolder();
        assert album != null && destination != null;
        try {
            sync.sync(album, destination);
        } catch (GoogleAuthException | IOException e) {
            Log.e(TAG, e.getMessage());
            this.error = true;
        }
        return null;
    }

    public void publicPublish() {
        this.publishProgress();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.error = false;
        presenter.setProgressBarVisibility(ProgressBar.VISIBLE);
        presenter.updateUI_CallResult(sync, SyncStep.STARTING);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        presenter.updateUI_CallResult(sync, SyncStep.IN_PRORGESS);
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        if (error) {
            presenter.showError();
        } else {
            presenter.setProgressBarVisibility(ProgressBar.INVISIBLE);
            presenter.updateUI_CallResult(sync, SyncStep.FINISHED);
        }
    }
}
