package gnn.com.googlealbumdownloadappnougat.tasks;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public class GetAlbumsTask extends PhotosAsyncTask<Void, Void, ArrayList<String>> {

    private final PhotosRemoteService prs;

    public GetAlbumsTask(IPresenterHome presenter, PhotosRemoteService prs, Context context) {
        super(presenter, context);
        this.prs = prs;
    }

    // For test
    public GetAlbumsTask(IPresenterHome presenter, PhotosRemoteService prs, Context context, PersistOauthError persistOauth) {
        super(presenter, context, persistOauth);
        this.prs = prs;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        try {
            return execOauth();
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            markAsError(e.toString());
            return null;
        }
    }

    @Override
    public ArrayList<String> returnFailure() {
        return null;
    }

    @Override
    public ArrayList<String> execOauthImpl() throws RemoteException {
        ArrayList<String> albumNames = null;
        albumNames = prs.getAlbums();
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
