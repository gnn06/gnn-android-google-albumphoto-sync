package gnn.com.googlealbumdownloadappnougat.tasks;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public class GetAlbumsTask extends PhotosAsyncTask<Void, Void, ArrayList<String>> {

    private PhotosRemoteService prs;

    public GetAlbumsTask(IPresenterHome presenter, PhotosRemoteService prs, Context context) {
        super(presenter, context);
        this.prs = prs;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> albumNames = null;
        try {
            albumNames = prs.getAlbums();
        } catch (RemoteException e) {
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
