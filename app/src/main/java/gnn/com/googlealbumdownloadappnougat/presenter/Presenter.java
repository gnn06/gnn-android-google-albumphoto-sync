package gnn.com.googlealbumdownloadappnougat.presenter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
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
import gnn.com.googlealbumdownloadappnougat.view.IView;
import gnn.com.photos.sync.DiffCalculator;
import gnn.com.photos.sync.Synchronizer;

public class Presenter implements IPresenter{

    private static final String TAG = "goi";

    private final IView view;
    private final MainActivity activity;
    private Context contextWrapper;

    public Presenter(IView view, MainActivity activity, Context contextWrapper) {
        this.view = view;
        this.activity = activity;
        this.contextWrapper  = contextWrapper;
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

    @Override
    public String getAlbum() {
        return album;
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
            String token = GoogleAuthUtil.getToken(contextWrapper.getApplicationContext(), account.getAccount(), "oauth2:profile email");
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

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask, MainActivity mainActivity) {
        Log.d(TAG, "handleSignInResult");
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        mainActivity.updateUI_User();
        if (!GoogleSignIn.hasPermissions(
                account,
                mainActivity.SCOPE_PHOTOS_READ)) {
            Log.d(TAG, "signin done, do not have permissions => request permissions");
            GoogleSignIn.requestPermissions(
                    mainActivity,
                    MainActivity.RC_AUTHORIZE_PHOTOS,
                    account,
                    mainActivity.SCOPE_PHOTOS_READ);
        } else {
            Log.d(TAG, "signin done, have permissions => laucnhSync()");
            laucnhSync();
        }
    }
    @Override
    public void handleAuthorizeWrite(MainActivity mainActivity) {
        Log.d(TAG, "handle write permission");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mainActivity);
        assert account != null;
        launchSynchWithPermission();
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
    public void onSyncClick() {
        Log.d(TAG, "onSyncClick");
        if (GoogleSignIn.getLastSignedInAccount(activity) == null) {
            Log.d(TAG,"onSyncClick, user does not be connected => SignInIntent");
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            Log.d(TAG, "start signin intent");
            view.updateUI_User();
            activity.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN);
        } else {
            if (!GoogleSignIn.hasPermissions(
                    GoogleSignIn.getLastSignedInAccount(activity),
                    activity.SCOPE_PHOTOS_READ)) {
                Log.d(TAG,"onSyncClick, user already signin, do not have permissions => requestPermissions");
                // doc said that if account is null, should ask but instead cancel the request or create a bug.
                GoogleSignIn.requestPermissions(
                        activity,
                        MainActivity.RC_AUTHORIZE_PHOTOS,
                        GoogleSignIn.getLastSignedInAccount(activity),
                        activity.SCOPE_PHOTOS_READ);
            } else {
                Log.d(TAG,"onSyncClick, user already signin, already have permissions => laucnhSync()");
                laucnhSync();
            }
        }
    }

    public void laucnhSync() {
        Log.d(TAG, "laucnhSync");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        assert account != null;
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "request WRITE permission");
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.RC_AUTHORIZE_WRITE);
        } else {
            Log.d(TAG,"WRITE permission granted, call google");
            launchSynchWithPermission();
        }
    }
    @Override
    public void launchSynchWithPermission() {
        String album = getAlbum();
        if (album.equals("")) {
            new AlertDialog.Builder(activity)
                    .setTitle("You have to choose an album")
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
        } else {
            SyncTask task = new SyncTask();
            task.execute();
        }
    }
    private class SyncTask extends AsyncTask<Void, Void, DiffCalculator> {



        @Override
        protected DiffCalculator doInBackground(Void... voids) {
            DiffCalculator diff = null;
            try {
                String album = getAlbum();
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
            activity.setSyncProgresBarVisibility(ProgressBar.VISIBLE);
            view.updateUI_CallResult("in progress");
        }

        @Override
        protected void onPostExecute(DiffCalculator sync) {
            super.onPostExecute(sync);
            activity.setSyncProgresBarVisibility(ProgressBar.INVISIBLE);
            String result = "";
            result += "synchronisation terminée avec succés\n";
            result += "downloaded = " + sync.getToDownload().size();
            result += "\n";
            result += "deleted = " + sync.getToDelete().size();
            view.updateUI_CallResult(result);
        }

    }
}

