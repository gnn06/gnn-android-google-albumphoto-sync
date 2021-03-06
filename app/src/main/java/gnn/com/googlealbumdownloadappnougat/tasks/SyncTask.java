package gnn.com.googlealbumdownloadappnougat.tasks;

import android.util.Log;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public class SyncTask extends PhotosAsyncTask<Void, Void, Void> {

    protected SynchronizerAndroid sync;

    public SyncTask(IPresenterMain presenter, SynchronizerAndroid sync) {
        super(presenter);
        this.sync = sync;
        sync.setSyncTask(this);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String album = presenter.getAlbum();
        File destination = presenter.getFolder();
        assert album != null && destination != null;
        String rename = presenter.getRename();
        int quantity = presenter.getQuantity();
        try {
            if (quantity == -1) {
                sync.syncAll(album, destination, rename);
            } else {
                sync.syncRandom(album, destination, rename, quantity);
            }
        } catch (Exception e) {
            // Catch Exception versus Google or IO to catch permission denied
            Log.e(TAG, e.toString());
            markAsError(e.toString());
        }
        return null;
    }

    public void publicPublish() {
        this.publishProgress();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        presenter.setSyncResult(sync, SyncStep.STARTING);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        presenter.setSyncResult(sync, SyncStep.IN_PRORGESS);
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        if (isSuccessful()) {
            presenter.setSyncResult(sync, SyncStep.FINISHED);
        }
    }
}
