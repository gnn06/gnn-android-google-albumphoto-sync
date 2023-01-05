package gnn.com.googlealbumdownloadappnougat.tasks;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerTask;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.photos.service.RemoteException;

public class SyncTask extends PhotosAsyncTask<Void, Void, Void> {

    // given from Presenter
    protected SynchronizerTask sync;
    final private PersistPrefMain persist;

    public SyncTask(IPresenterHome presenter, SynchronizerTask sync, PersistPrefMain persist, Context context) {
        super(presenter, context);
        this.sync = sync;
        sync.setSyncTask(this);
        this.persist = persist;
    }

    // For test
    public SyncTask(IPresenterHome presenter, SynchronizerTask sync, PersistPrefMain persist, Context context, PersistOauthError persistOauth) {
        super(presenter, context, persistOauth);
        this.sync = sync;
        sync.setSyncTask(this);
        this.persist = persist;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            return execOauth();
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            markAsError(e.toString());
        }
        return null;
    }

    @Override
    public Void returnFailure() {
        return null;
    }

    @Override
    public Void execOauthImpl() throws RemoteException {
        try {
            String album = presenter.getAlbum();
            File destination = presenter.getFolder();
            assert album != null && destination != null;
            String rename = persist.getRename();
            int quantity = persist.getQuantity();
            if (quantity == -1) {
                sync.syncAll(album, destination, rename);
            } else {
                sync.syncRandom(album, destination, rename, quantity);
            }
        } catch (IOException e) {
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
        presenter.setSyncResult(sync.getSyncData(), SyncStep.STARTING);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        presenter.setSyncResult(sync.getSyncData(), SyncStep.IN_PRORGESS);
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        if (isSuccessful()) {
            presenter.setSyncResult(sync.getSyncData(), SyncStep.FINISHED);
            presenter.refreshLastTime();
        }
    }
}
