package gnn.com.googlealbumdownloadappnougat.presenter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.view.IView;

public class Presenter implements IPresenter{

    private static final String TAG = "goi";

    private final IView view;
    private final MainActivity activity;

    public Presenter(IView view, MainActivity activity) {
        this.view = view;
        this.activity = activity;
    }

    private ArrayList<String> mAlbums;
    private String album;

    @Override
    public void onAlbumChoose() {
        if (mAlbums == null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.activity);
            assert account != null;
            GetAlbumsTask task = new GetAlbumsTask();
            task.execute();
        } else {
            Log.d(TAG, "choose albums from cache");
            view.showChooseAlbumDialog(mAlbums);
        }
    }

    private class GetAlbumsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> albumNames = new ArrayList<>();
            try {
                PhotosLibraryClient client = activity.getPhotoLibraryClient();
                InternalPhotosLibraryClient.ListAlbumsPagedResponse albums = client.listAlbums();
                for (Album album : albums.iterateAll()) {
                    albumNames.add(album.getTitle());
                }
            } catch (GoogleAuthException | IOException e) {
                Log.e(TAG, "can't get photo library client");
            }
            return albumNames;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.onGetAlbumsProgressBar(ProgressBar.VISIBLE);
        }

        @Override
        protected void onPostExecute(final ArrayList<String> albums) {
            super.onPostExecute(albums);
            view.onGetAlbumsProgressBar(ProgressBar.INVISIBLE);
            view.showChooseAlbumDialog(albums);
            mAlbums = albums;
        }
    }

    @Override
    public void onAlbumChoosen(String albumName) {
        // TODO: 05/06/2019 call service
        this.album = albumName;
        view.onAlbumChoosenResult(albumName);
    }

    @Override
    public String getAlbum() {
        return album;
    }
}

