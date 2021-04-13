package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.service.ScheduleActivity;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.photos.PhotosRemoteServiceAndroid;
import gnn.com.googlealbumdownloadappnougat.tasks.GetAlbumsTask;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermission;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public class PresenterMain implements IPresenterMain, IPresenterSettings {

    private static final String TAG = "goi";

    private final IView view;
    private final MainActivity activity;

    public PresenterMain(IView view, MainActivity activity) {
        this.view = view;
        this.activity = activity;
        this.auth = new AuthManager(activity);
    }

    private AuthManager auth;
    // For test

    public void setAuth(AuthManager auth) {
        this.auth = auth;
    }

    private SynchronizerAndroid sync;

    public SynchronizerAndroid getSync() {
        if (this.sync == null) {
            this.sync = new SynchronizerAndroid(activity, getCacheFile(), getCacheMaxAge(), getProcessFolder());
        }
        return sync;
    }

    private File cacheFile;
    private long cacheMaxAge; // default value come from Preferences

    /**
     * Get File to store cache
     * @return File
     */
    private File getCacheFile() {
        if (this.cacheFile == null) {
            File dir = activity.getApplicationContext().getFilesDir();
            // Example : "/data/user/0/gnn.com.googlealbumdownloadappnougat/files"
            this.cacheFile = new File(dir, "cache");
            Log.d(TAG, "cache dir = " + this.cacheFile.getAbsolutePath());
        }
        return this.cacheFile;
    }

    private File getProcessFolder() {
        // Example : "/data/user/0/gnn.com.googlealbumdownloadappnougat/files"
        return activity.getApplicationContext().getFilesDir();
    }

    @Override
    public void init() {
        view.updateUI_User();

        String lastSyncTime = getSync().retrieveLastSyncTime();
        view.updateUI_lastSyncTime(lastSyncTime);
    }

    @Override
    public void onSignIn() {
        Require require = new SignInRequirement(null, auth, view);
        startRequirement(require);
    }

    @Override
    public void onSignOut() {
        auth.signOut();
        getSync().resetCache();
        view.updateUI_User();
    }

    @Override
    public void showError(String message) {
        view.showError(message);
    }

    @Override
    public void setProgressBarVisibility(int visible) {
        view.setProgressBarVisibility(visible);
    }

    @Override
    public void setSyncResult(SynchronizerAndroid sync, SyncStep step) {
        view.updateUI_CallResult(sync, step);
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

    @Override
    public void onShowAlbumList() {
        if (mAlbums == null) {
            PhotosRemoteService prs = new PhotosRemoteServiceAndroid(activity, getCacheFile(), getCacheMaxAge());
            final GetAlbumsTask task = new GetAlbumsTask(this, prs);
            Exec exec = new Exec() {
                @Override
                public void exec() {
                    task.execute();
                }
            };
            Require signInReq = new SignInRequirement(exec, auth, view);
            startRequirement(signInReq);
        } else {
            Log.d(TAG, "choose albums from cache");
            view.showChooseAlbumDialog(mAlbums);
        }
    }

    /**
     * called when albums is retrieved
     * ask user to choose an album
     */
    @Override
    public void setAlbums(ArrayList<String> albums) {
        this.mAlbums = albums;
        view.showChooseAlbumDialog(albums);
    }

    // Use from persistence
    @Override
    public void setAlbum(String album) {
        this.album = album;
        view.onAlbumChosenResult(album);
    }

    /**
     * Called from alertDialog showing album list
     * @param albumName chosen album
     */
    @Override
    public void onChooseAlbum(String albumName) {
        this.album = albumName;
        getSync().resetCache();
        view.onAlbumChosenResult(albumName);
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
    public void setFolder(@Nonnull Intent data) {
        Uri uri = data.getData();
        assert uri != null;
        final String humanPath = Folder.getHumanPath(uri);
        Log.d(TAG,"humanPath=" + humanPath);
        this.folderHuman = humanPath;
        view.updateUI_Folder(humanPath);
    }

    // --- quantity ---

    /**
     * @return -1 if no quantity specified
     */
    @Override
    public int getQuantity() {
        String quantity = view.getQuantity();
        // replace "" into "-1"
        return Integer.parseInt(quantity.equals("") ? "-1" : quantity);
    }

    @Override
    public void setQuantity(int quantity) {
        // replace -1 into ""
        view.setQuantity(quantity == -1 ? "" : Integer.toString(quantity));
    }

    @Override
    public String getRename() {
        return view.getRename();
    }

    public void setRename(String rename) {
        view.setRename(rename);
    }

    public long getCacheMaxAge() {
        return this.cacheMaxAge;
    }

    @Override
    public void setCacheMaxAge(long cacheMaxAge) {
        this.cacheMaxAge = cacheMaxAge;
    }

    // --- Actions ---

    @Override
    public void onSyncClick() {
        Log.d(TAG, "onSyncClick");
        String album = this.album;
        if (album == null || album.equals("")) {
            view.alertNoAlbum();
        } else {
            taskWithPermissions(new SyncTask(this, getSync()));
        }
    }

    @Override
    public void onChooseSync() {
        Log.d(TAG, "onChooseOneClick");
        String album = this.album;
        if (album == null || album.equals("")) {
            view.alertNoAlbum();
        } else {
            taskWithPermissions(new SyncTask(this, getSync()));
        }
    }

    @Override
    public void onResetCache() {
        getSync().resetCache();
    }

    @Override
    public void onButtonSchedule() {
        Intent intent = new Intent(activity, ScheduleActivity.class);
//        intent.putE
        activity.startActivity(intent);
    }
    // --- private methods ---

    private void taskWithPermissions(final SyncTask task) {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                task.execute();
            }
        };
        Require writeReq = new WritePermission(exec, auth, view);
        Require signInReq = new SignInRequirement(writeReq, auth, view);
        startRequirement(signInReq);
    }

    // --- Requirement ---

    private Require pendingRequirement;

    void startRequirement(Require require) {
        this.pendingRequirement = require;
        require.exec();
    }

    Require getPendingRequirement() {
        return this.pendingRequirement;
    }

    @Override
    public void handlePermission(int result) {
        if (pendingRequirement != null) {
            pendingRequirement.resumeRequirement(result);
        }
    }
}

