package gnn.com.googlealbumdownloadappnougat.presenter;

import android.content.Intent;
import android.net.Uri;
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
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermission;
import gnn.com.googlealbumdownloadappnougat.view.IView;
import gnn.com.photos.sync.Synchronizer;

public class Presenter implements IPresenter{

    private static final String TAG = "goi";

    private final IView view;
    private final MainActivity activity;
    private AuthManager auth;

    public Presenter(IView view, MainActivity activity) {
        this.view = view;
        this.activity = activity;
        this.auth = new AuthManager(activity);
    }

    // For test
    public void setAuth(AuthManager auth) {
        this.auth = auth;
    }


    @Override
    public void onSignIn() {
        Require require = new SignInRequirement(null, auth, view);
        setPendingRequirement(require);
        require.exec();
    }

    @Override
    public void onSignOut() {
        auth.signOut();
        view.updateUI_User();
    }

    // --- Album ---

    private ArrayList<String> mAlbums;

    /**
     * default value = null
     * persistent data
     */
    private String album;

    @Override
    public String getAlbum() {
        return album;
    }

    // Use from persistence
    @Override
    public void setAlbum(String album) {
        this.album = album;
        view.onAlbumChoosenResult(album);
    }

    @Override
    public void onShowAlbumList() {
        if (mAlbums == null) {
            Exec exec = new Exec() {
                @Override
                public void exec() {
                    GetAlbumsTask task = new GetAlbumsTask();
                    task.execute();
                }
            };
            Require signInReq = new SignInRequirement(exec, auth, view);
            setPendingRequirement(signInReq);
            signInReq.exec();
        } else {
            Log.d(TAG, "choose albums from cache");
            view.showChooseAlbumDialog(mAlbums);
        }
    }

    /**
     * Called from alertDialog showing album list
     * @param albumName
     */
    @Override
    public void onChooseAlbum(String albumName) {
        this.album = albumName;
        view.onAlbumChoosenResult(albumName);
    }

    // --- folder ---

    /**
     * default value = "Pictures"
     * persistant data
     */
    private String folderHuman = Environment.DIRECTORY_PICTURES;

    /**
     * Get File from human version of folder
     * @return File
     */
    private File getFolder() {
        return Environment.getExternalStoragePublicDirectory(folderHuman);
    }

    @Override
    public String getFolderHuman() {
        return this.folderHuman;
    }

    // Use from Persistence
    @Override
    public void setFolderHuman(String folderHuman) {
        this.folderHuman = folderHuman;
        view.updateUI_Folder(folderHuman);
    }

    @Override
    public void chooseFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        this.activity.startActivityForResult(intent, MainActivity.RC_CHOOSE_FOLDER);
    }

    @Override
    public void chooseFolderResult(Intent data) {
            Uri uri = data.getData();
            final String humanPath = Folder.getHumanPath(uri);
            Log.d(TAG,"humanPath=" + humanPath);
            this.folderHuman = humanPath;
            view.updateUI_Folder(humanPath);
    }

    // --- sync ---

    @Override
    public void onSyncClick() {
        Log.d(TAG, "onSyncClick");
        String album = this.album;
        if (album == null || album.equals("")) {
            view.alertNoAlbum();
        } else {
            Exec exec = new Exec() {
                @Override
                public void exec() {
                    SyncTask task = new SyncTask();
                    task.execute();
                }
            };
            Require writeReq = new WritePermission(exec, auth, view);
            Require signInReq = new SignInRequirement(writeReq, auth, view);
            // TODO: 30/07/2019 peut-on mémoriser le requirement et le lancer
            setPendingRequirement(signInReq);
            signInReq.exec();
        }
    }

    // --- Async Task ---

    // TODO: 30/07/2019 mutualise code pour récupérer GoogleClient
    private class GetAlbumsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> albumNames = new ArrayList<>();
            // TODO: 30/07/2019 manage exceptions
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
            view.setProgressBarVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onPostExecute(final ArrayList<String> albums) {
            super.onPostExecute(albums);
            view.setProgressBarVisibility(ProgressBar.INVISIBLE);
            view.showChooseAlbumDialog(albums);
            mAlbums = albums;
        }
    }

    public class SyncTask extends AsyncTask<Void, Void, Void> {

        Synchronizer sync = new Synchronizer(this);

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: 30/07/2019 manage exceptions
            try {
                String album = Presenter.this.album;
                File destination = getFolder();
                assert album != null && destination != null;
                PhotosLibraryClient client = getPhotoLibraryClient();
                sync.sync(album, destination, client);
            } catch (GoogleAuthException | IOException e) {
                Log.e(TAG, "can't get photo library client");
            }
            return null;
        }

        public void publicPublish() {
            this.publishProgress();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.setProgressBarVisibility(ProgressBar.VISIBLE);
            view.updateUI_CallResult(sync, SyncStep.STARTING);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            view.updateUI_CallResult(sync, SyncStep.IN_PRORGESS);
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            view.setProgressBarVisibility(ProgressBar.INVISIBLE);
            view.updateUI_CallResult(sync, SyncStep.FINISHED);
            // TODO: 30/07/2019 Does it isusefull to store syncrhonizer to store result ?
        }
    }

    // TODO: 30/07/2019 singleton
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
            // TODO: 31/07/2019 manager error
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

    // --- Requirement ---

    private Require pendingRequirement;

    private void setPendingRequirement(Require require) {
        this.pendingRequirement = require;
    }

    @Override
    public void handlePermission(int result) {
        // TODO: 09/06/2019 manage cancel, error
        if (pendingRequirement != null) {
            pendingRequirement.resumeRequirement(result);
        }
    }
}

