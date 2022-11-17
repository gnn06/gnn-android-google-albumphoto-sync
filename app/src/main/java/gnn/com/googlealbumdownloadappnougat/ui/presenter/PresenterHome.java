package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.GoogleAuthRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInGoogleAPIRequirementBuilder;
import gnn.com.googlealbumdownloadappnougat.auth.SignInGoogleAPIWriteRequirementBuilder;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermissionRequirement;
import gnn.com.googlealbumdownloadappnougat.photos.PhotosRemoteServiceAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerTask;
import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;
import gnn.com.googlealbumdownloadappnougat.settings.IPresenterSettings;
import gnn.com.googlealbumdownloadappnougat.settings.PersistPrefSettings;
import gnn.com.googlealbumdownloadappnougat.tasks.GetAlbumsTask;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.googlealbumdownloadappnougat.ui.FolderModel;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewHome;
import gnn.com.googlealbumdownloadappnougat.wallpaper.MyWallpaperService;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.stat.stat.WallpaperStatProvider;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;
import gnn.com.photos.sync.PersistWallpaperTime;
import gnn.com.photos.sync.Temp;

public class PresenterHome implements IPresenterHome, IPresenterSettings {

    private static final String TAG = "goi";

    private final MainActivity activity;
    private final FragmentHome fragment;
    private UserModel userModel;
    private FolderModel folderModel;

    public PresenterHome(MainActivity activity, FragmentHome fragmentHome) {
        this.activity = activity;
        this.auth = new AuthManager(activity);
        this.fragment = fragmentHome;
        userModel = new ViewModelProvider(fragmentHome).get(UserModel.class);
        folderModel = new ViewModelProvider(fragmentHome).get(FolderModel.class);
    }

    public PresenterHome(IViewHome view, MainActivity activity, FragmentHome fragmentHome,
                         UserModel userModel, FolderModel folderModel) {
        this.activity = activity;
        this.fragment = fragmentHome;
        this.auth = new AuthManager(activity);
        this.userModel = userModel;
        this.folderModel = folderModel;
    }

    private AuthManager auth;

    // For test
    public void setAuth(AuthManager auth) {
        this.auth = auth;
    }

    private SynchronizerTask sync;

    // TODO replace with injection
    public SynchronizerTask getSync() {
        if (this.sync == null) {
            PersistPrefMain persistPref = new PersistPrefMain(this.activity);
            this.sync = new SynchronizerTask(activity, getCacheFile(), persistPref.getFrequencyUpdatePhotos(), getProcessFolder());
        }
        return sync;
    }

    private File cacheFile;

    /**
     * Get File to store cache
     *
     * @return File
     */
    private File getCacheFile() {
        if (this.cacheFile == null) {
            File dir = ApplicationContext.getInstance(activity).getProcessFolder();
            // Example : "/data/user/0/gnn.com.googlealbumdownloadappnougat/files"
            this.cacheFile = new File(dir, "cache");
            Log.d(TAG, "cache dir = " + this.cacheFile.getAbsolutePath());
        }
        return this.cacheFile;
    }

    private File getProcessFolder() {
        // Example : "/data/user/0/gnn.com.googlealbumdownloadappnougat/files"
        return ApplicationContext.getInstance(activity).getProcessFolder();
    }

    // called by onCreate
    @Override
    public void onAppStart() {
        if (folderModel.getFolder().getValue() == null) {
            folderModel.getFolder().setValue(defaultFolderHuman);
        }
        new PersistPrefMain(activity).restore(this);
        new PersistPrefSettings(activity).restore(this);

        new WallpaperScheduler(activity).registerWorkerObserver(this, fragment);
        new SyncScheduler(activity).registerWorkerObserver(this, fragment);
    }

    @Override
    public void onAppForeground() {
        fragment.updateUI_User();
        refreshLastTime();
        WallpaperStat stat = new WallpaperStatProvider(getProcessFolder()).get();
        fragment.setStat(stat);

        WallpaperManager wlppMgr = WallpaperManager.getInstance(activity);
        WallpaperInfo wlppInfo = wlppMgr != null ? wlppMgr.getWallpaperInfo() : null;
        Log.d("GOI-WALLPAPER", "wallpaperinfo.packagename=" +
                (wlppInfo != null ? wlppInfo.getPackageName() : "no package"));
        fragment.setWarningWallpaperActive(new MyWallpaperService().isActive(this.activity));

    }

    /**
     * Called on onStart activity
     * Used to refresh UI
     */
    @Override
    public void refreshLastTime() {
        Log.d(TAG, "refresh UI");
        Date lastUpdateTime = Cache.getLastUpdateTime(getCacheFile());
        Date lastSyncTime = getSync().retrieveLastSyncTime();
        Date lastWallpaperTime = new PersistWallpaperTime(getProcessFolder()).retrieveTime();
        fragment.updateUI_lastSyncTime(lastUpdateTime, lastSyncTime, lastWallpaperTime);
    }

    @Override
    public void showError(String message) {
        fragment.showError(message);
    }

    @Override
    public void setProgressBarVisibility(int visible) {
        fragment.setProgressBarVisibility(visible);
    }

    @Override
    public void setSyncResult(Temp syncData, SyncStep step) {
        fragment.updateUI_CallResult(syncData, step);
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
            PersistPrefMain persist = new PersistPrefMain(this.activity);
            PhotosRemoteService prs = new PhotosRemoteServiceAndroid(activity, getCacheFile(), persist.getFrequencyUpdatePhotos());
            final GetAlbumsTask task = new GetAlbumsTask(this, prs, activity);
            Exec exec = new Exec() {
                @Override
                public void exec() {
                    fragment.setProgressBarVisibility(ProgressBar.VISIBLE);
                    task.execute();
                }
            };
            Require signInReq = new GoogleAuthRequirement(exec, auth, fragment, userModel);
            activity.getPermissionHandler().startRequirement(signInReq);
        } else {
            Log.d(TAG, "choose albums from cache");
            fragment.showChooseAlbumDialog(mAlbums);
        }
    }

    /**
     * called when albums is retrieved
     * ask user to choose an album
     */
    @Override
    public void setAlbums(ArrayList<String> albums) {
        this.mAlbums = albums;
        fragment.setProgressBarVisibility(ProgressBar.INVISIBLE);
        fragment.showChooseAlbumDialog(albums);
    }

    // Use from persistence
    @Override
    public void setAlbum(String album) {
        this.album = album;
        fragment.onAlbumChosenResult(album);
    }

    /**
     * Called from alertDialog showing album list
     *
     * @param albumName chosen album
     */
    @Override
    public void onChooseAlbum(String albumName) {
        this.album = albumName;
        getSync().resetCache();
        fragment.onAlbumChosenResult(albumName);
    }


    // --- folder ---
    /**
     * default value = "Pictures"
     * persistant data
     */
    private String defaultFolderHuman = Environment.DIRECTORY_PICTURES;

    @Override
    public String getDefaultFolderHuman() {
        return folderModel.getFolder().getValue();
    }

    // Use from Persistence

    /**
     * Get File from human version of folder
     *
     * @return File
     */
    @Override
    public File getFolder() {
        return Environment.getExternalStoragePublicDirectory(this.getDefaultFolderHuman());
    }

    @Override
    public void setDefaultFolderHuman(String defaultFolderHuman) {
        folderModel.getFolder().setValue(defaultFolderHuman);
    }

    @Override
    public void onChooseFolder() {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                fragment.startActivityForResult(intent, FragmentHome.RC_CHOOSE_FOLDER);
            }
        };
        WritePermissionRequirement requirement = new WritePermissionRequirement(exec, auth, fragment, userModel);
        activity.getPermissionHandler().startRequirement(requirement);
    }

    @Override
    public void setFolder(@Nonnull Intent data) {
        Uri uri = data.getData();
        assert uri != null;
        final String humanPath = Folder.getHumanPath(uri);
        Log.d(TAG, "humanPath=" + humanPath);
        folderModel.getFolder().setValue(humanPath);
    }

    // --- Actions ---

    @Override
    public void onButtonSyncOnce() {
        Log.d(TAG, "onSyncClick");
        String album = this.album;
        if (album == null || album.equals("")) {
            fragment.alertNoAlbum();
        } else {
            SyncTask task = new SyncTask(this, getSync(), new PersistPrefMain(activity.getApplicationContext()), activity);
            Require signInReq = SignInGoogleAPIWriteRequirementBuilder.build(task, auth, fragment, userModel);
            activity.getPermissionHandler().startRequirement(signInReq);
        }
    }

    @Override
    public void onButtonWallpaperOnce() {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                // TODO check folder is not null
                ChooseOneLocalPhotoPersist chooser = ChooseOneLocalPhotoPersist.getInstance(getFolder(), getProcessFolder());
                chooser.chooseOne();
            }
        };
        Require require = new WritePermissionRequirement(exec, auth, fragment, userModel);
        activity.getPermissionHandler().startRequirement(require);
    }

    @Override
    public void onWarningWallpaperActive() {
        try {
            fragment.startActivity(new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                    .putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(activity,
                                    MyWallpaperService.class))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            try {
                fragment.startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (ActivityNotFoundException e2) {
                // TODO manage error
                Toast.makeText(activity, R.string.error_wallpaper_chooser, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSignIn() {
        Require require = SignInGoogleAPIRequirementBuilder.build(null, auth, fragment, userModel);
        activity.getPermissionHandler().startRequirement(require);
    }

    public void onWorkerExecuted() {
        Log.d("GNNAPP", "PresenterHome.onWorkerExecuted");
        refreshLastTime();
    }
}

