package gnn.com.googlealbumdownloadappnougat.presenter;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.photos.sync.Synchronizer;

public class SyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "goi";

    private IPresenter presenter;
    public SyncTask(IPresenter presenter, MainActivity activity) {
        this.presenter = presenter;
        this.sync = new Synchronizer(this, activity);
    }

    private Synchronizer sync;
    /**
     * Get File from human version of folder
     * @return File
     */
    private File getFolder() {
        return Environment.getExternalStoragePublicDirectory(presenter.getFolderHuman());
    }

    private boolean error;

    @Override
    protected Void doInBackground(Void... voids) {
        // TODO: 30/07/2019 manage exceptions
        String album = presenter.getAlbum();
        File destination = getFolder();
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
