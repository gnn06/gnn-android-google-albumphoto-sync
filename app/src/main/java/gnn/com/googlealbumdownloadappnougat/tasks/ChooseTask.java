package gnn.com.googlealbumdownloadappnougat.tasks;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;

public class ChooseTask extends SyncTask {

    public ChooseTask(IPresenterMain presenter, SynchronizerAndroid sync) {
        super(presenter, sync);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String album = presenter.getAlbum();
        File destination = presenter.getFolder();
        int quantity = presenter.getQuantity();
        try {
            if (quantity == -1) {
                sync.syncAll(album, destination);
            } else {
                sync.syncRandom(album, destination, quantity);
            }
        } catch (GoogleAuthException | IOException e) {
            Log.e(TAG, e.toString());
            markAsError(e.toString());
        }
        return null;
    }
}
