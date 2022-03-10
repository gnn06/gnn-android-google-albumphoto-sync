package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.GoogleAuthRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInGoogleAPIWriteRequirementBuilder;
import gnn.com.googlealbumdownloadappnougat.auth.SignInRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermissionRequirement;
import gnn.com.googlealbumdownloadappnougat.photos.PhotosRemoteServiceAndroid;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.service.ActivitySchedule;
import gnn.com.googlealbumdownloadappnougat.settings.IPresenterSettings;
import gnn.com.googlealbumdownloadappnougat.settings.PersistPrefSettings;
import gnn.com.googlealbumdownloadappnougat.tasks.GetAlbumsTask;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;
import gnn.com.googlealbumdownloadappnougat.wallpaper.MyWallpaperService;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.CacheManager;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.stat.stat.WallpaperStatProvider;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;
import gnn.com.photos.sync.PersistWallpaperTime;

public class PresenterHome implements IPresenterHome, IPresenterSettings {

    private static final String TAG = "goi";

    private final IView view;
    private final MainActivity activity;
    private PermissionHandler permissionHandler;

    public PresenterHome(IView view, MainActivity activity, PermissionHandler permissionHandler) {
        this.view = view;
        this.activity = activity;
        this.auth = new AuthManager(activity);
        this.permissionHandler = permissionHandler;
    }

    private AuthManager auth;
    // For test

    public void setAuth(AuthManager auth) {
        this.auth = auth;
    }

    private SynchronizerAndroid sync;

    public SynchronizerAndroid getSync() {
        if (this.sync == null) {
            PersistPrefMain persistPref = new PersistPrefMain(this.activity);
            this.sync = new SynchronizerAndroid(activity, getCacheFile(), persistPref.getFrequencyUpdatePhotosHour(), getProcessFolder());
        }
        return sync;
    }

    private File cacheFile;

    /**
     * Get File to store cache
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
        new PersistPrefMain(activity).restore(this);
        new PersistPrefSettings(activity).restore(this);
    }

    @Override
    public void onAppForeground() {
        view.updateUI_User();
        refreshLastTime();
        WallpaperStat stat = new WallpaperStatProvider(getProcessFolder()).get();
        view.setStat(stat);
        WallpaperManager wlppMgr = WallpaperManager.getInstance(activity);
        WallpaperInfo wlppInfo = wlppMgr != null ? wlppMgr.getWallpaperInfo() : null;
        Log.d("GOI-WALLPAPER", "wallpaperinfo.packagename=" +
                (wlppInfo != null ? wlppInfo.getPackageName() : "no package"));
        view.setWarningWallpaperActive(wlppInfo != null && wlppInfo.getPackageName().equals("gnn.com.googlealbumdownloadappnougat"));

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
        view.updateUI_lastSyncTime(lastUpdateTime, lastSyncTime, lastWallpaperTime);
    }

    @Override
    public void onSignIn() {
        Require require = new SignInRequirement(null, auth, view);
        permissionHandler.startRequirement(require);
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
            PersistPrefMain persist = new PersistPrefMain(this.activity);
            PhotosRemoteService prs = new PhotosRemoteServiceAndroid(activity, getCacheFile(), persist.getFrequencyUpdatePhotosHour());
            final GetAlbumsTask task = new GetAlbumsTask(this, prs, persist);
            Exec exec = new Exec() {
                @Override
                public void exec() {
                    task.execute();
                }
            };
            Require signInReq = new GoogleAuthRequirement(exec, auth, view);
            permissionHandler.startRequirement(signInReq);
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
    public void onChooseFolder() {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                activity.startActivityForResult(intent, MainActivity.RC_CHOOSE_FOLDER);
            }
        };
        WritePermissionRequirement requirement = new WritePermissionRequirement(exec, auth, view);
        permissionHandler.startRequirement(requirement);
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

    // --- Actions ---

    @Override
    public void onButtonSyncOnce() {
        Log.d(TAG, "onSyncClick");
        String album = this.album;
        if (album == null || album.equals("")) {
            view.alertNoAlbum();
        } else {
            SyncTask task = new SyncTask(this, getSync(), new PersistPrefMain(activity.getApplicationContext()));
            Require signInReq = SignInGoogleAPIWriteRequirementBuilder.build(task, auth, view);
            permissionHandler.startRequirement(signInReq);
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
        Require require = new WritePermissionRequirement(exec, auth, view);
        permissionHandler.startRequirement(require);
    }

    @Override
    public void onMenuResetCache() {
        new CacheManager(getProcessFolder()).resetAll();
        refreshLastTime();
    }

    @Override
    public void onMenuScheduleDetail() {
        Intent intent = new Intent(activity, ActivitySchedule.class);
//        intent.putE
        activity.startActivity(intent);
    }

    @Override
    public void onMenuRequestGooglePermission() {
        Require require = new GoogleAuthRequirement(null, auth, view);
        permissionHandler.startRequirement(require);
    }

    @Override
    public void onWarningWallpaperActive() {
        try {
            activity.startActivity(new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                    .putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(activity,
                                    MyWallpaperService.class))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            try {
                activity.startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (ActivityNotFoundException e2) {
                // TODO manage error
                Toast.makeText(activity, R.string.error_wallpaper_chooser, Toast.LENGTH_LONG).show();
            }
        }
    }
}

