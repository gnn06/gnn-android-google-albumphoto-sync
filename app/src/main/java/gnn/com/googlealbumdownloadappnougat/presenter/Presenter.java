package gnn.com.googlealbumdownloadappnougat.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermission;
import gnn.com.googlealbumdownloadappnougat.view.IView;
import gnn.com.photos.remote.PhotosRemoteService;
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
            PhotosRemoteService prs = new PhotosRemoteService(activity);
            final GetAlbumsTask task = new GetAlbumsTask(this, prs);
            Exec exec = new Exec() {
                @Override
                public void exec() {
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

    @Override
    public void setAlbums(ArrayList<String> albums) {
        this.mAlbums = albums;
        view.showChooseAlbumDialog(albums);
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

    public class SyncTask extends AsyncTask<Void, Void, Void> {

        Synchronizer sync = new Synchronizer(this, activity);

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: 30/07/2019 manage exceptions
            String album = Presenter.this.album;
            File destination = getFolder();
            assert album != null && destination != null;
            sync.sync(album, destination);
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

    @Override
    public void showError() {
        view.showError();
    }

    @Override
    public void setProgressBarVisibility(int visible) {
        view.setProgressBarVisibility(visible);
    }


}

