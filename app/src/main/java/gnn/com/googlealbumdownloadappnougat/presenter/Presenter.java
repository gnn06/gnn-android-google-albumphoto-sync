package gnn.com.googlealbumdownloadappnougat.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nonnull;

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
        view.onAlbumChosenResult(album);
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
     * @param albumName chosen album
     */
    @Override
    public void onChooseAlbum(String albumName) {
        this.album = albumName;
        view.onAlbumChosenResult(albumName);
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

    @Override
    public String getFolderHuman() {
        return this.folderHuman;
    }
    // Use from Persistence

    /**
     * Get File from human version of folder
     * @return File
     */
    @Override
    public File getFolder() {
        return Environment.getExternalStoragePublicDirectory(this.getFolderHuman());
    }

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
    public void chooseFolderResult(@Nonnull Intent data) {
        Uri uri = data.getData();
        assert uri != null;
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
            Synchronizer synchro = new Synchronizer(activity);
            final SyncTask task = new SyncTask(this, synchro);
            Exec exec = new Exec() {
                @Override
                public void exec() {
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

    @Override
    public void updateUI_CallResult(Synchronizer sync, SyncStep starting) {
        view.updateUI_CallResult(sync, starting);
    }


}

