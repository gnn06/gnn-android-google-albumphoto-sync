package gnn.com.googlealbumdownloadappnougat.tasks;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public class SyncTask extends PhotosAsyncTask<Void, Void, Void> {

    private SynchronizerAndroid sync;

    public SyncTask(IPresenter presenter, SynchronizerAndroid sync) {
        super(presenter);
        this.sync = sync;
        sync.setSyncTask(this);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String album = presenter.getAlbum();
        File destination = presenter.getFolder();
        assert album != null && destination != null;
        try {
            sync.sync(album, destination);
        } catch (GoogleAuthException | IOException e) {
            Log.e(TAG, e.getMessage());
            markAsError();
        }
        return null;
    }

    public void publicPublish() {
        this.publishProgress();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        if (isSuccessful()) {
            presenter.updateUI_CallResult(sync, SyncStep.FINISHED);
        }
    }
}
