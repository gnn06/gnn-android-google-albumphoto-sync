package gnn.com.googlealbumdownloadappnougat.presenter;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.GooglePhotoAPI_Requirement;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.SignRquirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermission;
import gnn.com.googlealbumdownloadappnougat.view.IView;
import gnn.com.photos.sync.DiffCalculator;
import gnn.com.photos.sync.Synchronizer;

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
    public void onShowAlbumList() {
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

    @Override
    public void onChooseAlbum(String albumName) {
        // TODO: 05/06/2019 call service
        this.album = albumName;
        view.onAlbumChoosenResult(albumName);
    }

    private File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    @Override
    public File getFolder() {
        return folder;
    }

    private PhotosLibraryClient mClient;

    private PhotosLibraryClient getPhotoLibraryClient()
            throws IOException, GoogleAuthException
    {
        if (mClient != null) {
            Log.d(TAG, "get photo library client from cache");
            return mClient;
        } else {
            /* Need an Id client OAuth in the google developer console of type android
             * Put the package and the fingerprint (gradle signingReport)
             */
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
            assert account != null && account.getAccount() != null;
            String token = GoogleAuthUtil.getToken(activity.getApplicationContext(), account.getAccount(), "oauth2:profile email");
            OAuth2Credentials userCredentials = OAuth2Credentials.newBuilder()
                    .setAccessToken(new AccessToken(token, null))
                    .build();
            PhotosLibrarySettings settings =
                    PhotosLibrarySettings.newBuilder()
                            .setCredentialsProvider(
                                    FixedCredentialsProvider.create(
                                            userCredentials))
                            .build();
            PhotosLibraryClient client = PhotosLibraryClient.initialize(settings);
            mClient = client;
            return client;
        }
    }

    public void setPermissionRequirement(PermissionRequirement permissionRequirement) {
        this.permissionRequirement = permissionRequirement;
    }

    private PermissionRequirement permissionRequirement;

    public class TaskExec extends PermissionRequirement {
        public TaskExec() {
            super(null, null, null);
        }

        @Override
        public boolean checkRequirement() {
            return true;
        }

        @Override
        public void askAsyncRequirement() {

        }

        @Override
        public void exec() {
            Presenter.SyncTask task = new Presenter.SyncTask();
            task.execute();
        }
    }

    @Override
    public void onSyncClick() {
        Log.d(TAG, "onSyncClick");
        String album = this.album;
        if (album.equals("")) {
            view.alertNoAlbum();
        } else {
            AuthManager auth = new AuthManager(this.activity);
            PermissionRequirement taskExeq  = new TaskExec();
            PermissionRequirement writeReq  = new WritePermission(taskExeq, auth);
            PermissionRequirement photoReq  = new GooglePhotoAPI_Requirement(writeReq, view, auth);
            PermissionRequirement signInReq = new SignRquirement(photoReq, view, auth);
            setPermissionRequirement(signInReq);
            permissionRequirement = permissionRequirement.checkAndExec();
        }
    }

    @Override
    public void handlePermission() {
        // TODO: 09/06/2019 manage cancel, error
        // TODO: 09/06/2019 call update User ()
        if (permissionRequirement != null) {
            permissionRequirement = permissionRequirement.checkAndExec();
        }
    }

    private class GetAlbumsTask extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> albumNames = new ArrayList<>();
            try {
                PhotosLibraryClient client = getPhotoLibraryClient();
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
            view.setChooseAlbumProgressBarVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onPostExecute(final ArrayList<String> albums) {
            super.onPostExecute(albums);
            view.setChooseAlbumProgressBarVisibility(ProgressBar.INVISIBLE);
            view.showChooseAlbumDialog(albums);
            mAlbums = albums;
        }
    }

    public class SyncTask extends AsyncTask<Void, Void, DiffCalculator> {

        @Override
        protected DiffCalculator doInBackground(Void... voids) {
            DiffCalculator diff = null;
            try {
                String album = Presenter.this.album;
                File destination = getFolder();
                Synchronizer sync = new Synchronizer();
                PhotosLibraryClient client = getPhotoLibraryClient();
                diff = sync.sync(album, destination, client);
            } catch (GoogleAuthException | IOException e) {
                Log.e(TAG, "can't get photo library client");
            }
            return diff;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.setSyncProgresBarVisibility(ProgressBar.VISIBLE);
            view.updateUI_CallResult("in progress");
        }

        @Override
        protected void onPostExecute(DiffCalculator sync) {
            super.onPostExecute(sync);
            view.setSyncProgresBarVisibility(ProgressBar.INVISIBLE);
            String result = "";
            result += "synchronisation terminée avec succés\n";
            result += "downloaded = " + sync.getToDownload().size();
            result += "\n";
            result += "deleted = " + sync.getToDelete().size();
            view.updateUI_CallResult(result);
        }

    }
}

