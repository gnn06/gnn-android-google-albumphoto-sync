package gnn.com.googlealbumdownloadappnougat.tasks;

import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenter;

public class ChooseTask extends SyncTask {

    public ChooseTask(IPresenter presenter, SynchronizerAndroid sync) {
        super(presenter, sync);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String album = presenter.getAlbum();
        File destination = presenter.getFolder();
        assert album != null && destination != null;
        try {
            sync.chooseOne(album, destination);
        } catch (GoogleAuthException | IOException e) {
            Log.e(TAG, e.getMessage());
            markAsError();
        }
        return null;
    }
}
