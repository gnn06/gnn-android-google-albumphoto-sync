package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nonnull;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
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
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperSetter;
import gnn.com.photos.service.Cache;
import gnn.com.photos.service.CacheManager;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.stat.stat.WallpaperStatProvider;
import gnn.com.photos.sync.ChooseOneLocalPhotoPersist;
import gnn.com.photos.sync.PersistWallpaperTime;
import gnn.com.photos.sync.SynchronizerDelayed;

public class PresenterMain implements IPresenterMain, IPresenterSettings {

    private static final String TAG = "goi";

    private final IView view;
    private final MainActivity activity;
    private PermissionHandler permissionHandler;

    public PresenterMain(IView view, MainActivity activity, PermissionHandler permissionHandler) {
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
            this.sync = new SynchronizerAndroid(activity, getCacheFile(), getFrequencyUpdatePhotosHour(), getProcessFolder());
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
            File dir = activity.getApplicationContext().getFilesDir();
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

        // some init will be done in onAppForeground

        WallpaperScheduler scheduler = new WallpaperScheduler(this.activity);
        boolean scheduled = scheduler.isScheduled();
        view.setSwitchWallpaper(scheduled);
        view.enableFrequencyWallpaper(scheduled);
        view.enableFrequencySync(scheduled);
        view.enableFrequencyUpdatePhotos(scheduled);
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
            PhotosRemoteService prs = new PhotosRemoteServiceAndroid(activity, getCacheFile(), getFrequencyUpdatePhotosHour());
            final GetAlbumsTask task = new GetAlbumsTask(this, prs);
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

    /**
     *
     * @return in minute
     */
    @Override
    public int getFrequencyWallpaper() {
        String frequency = view.getFrequencyWallpaper();
        return Integer.parseInt(frequency.equals("") ? "-1" : frequency);
    }

    /**
     *
     * @param frequency in minute
     */
    @Override
    public void setFrequencyWallpaper(int frequency) {
        view.setFrequencyWallpaper(frequency == -1 ? "" : Integer.toString(frequency));
    }

    /**
     *
     * int frequencySync fréquence de téléchargement en heure
     */
    @Override
    public int getFrequencySync() {
        String frequency = view.getFrequencySync();
        return frequency.equals("") ? SynchronizerDelayed.DELAY_NO_CACHE : Integer.parseInt(frequency);
    }

    /**
     *
     * int frequencySync fréquence de téléchargement en minute
     */
    @Override
    public int getFrequencySyncMinute() {
        return getFrequencySync() * 60;
    }

    /**
     *
     * @param frequency in hour
     */
    @Override
    public void setFrequencySync(int frequency) {
        view.setFrequencySync(frequency == -1 ? "" : Integer.toString(frequency));
    }

    /**
     *
     * @return frequence de recupération des nouvelles phtos in days
     */
    @Override
    public int getFrequencyUpdatePhotos() {
        String frequency = view.getFrequencyUpdatePhotos();
        return frequency.equals("") ? Cache.DELAY_NO_CACHE : Integer.parseInt(frequency);

    }

    /**
     * @return fréquence en minute
     */
    @Override
    public int getFrequencyUpdatePhotosHour() {
        return getFrequencyUpdatePhotos() * 24 * 60;
    }

    /**
     *
     * @param frequency in days
     */
    @Override
    public void setFrequencyUpdatePhotos(int frequency) {
        view.setFrequencyUpdatePhotos(frequency == -1 ? "" : Integer.toString(frequency));
    }

    @Override
    public String getRename() {
        return view.getRename();
    }

    public void setRename(String rename) {
        view.setRename(rename);
    }

    // --- Actions ---

    @Override
    public void onSwitchWallpaper(boolean checked) {
        WallpaperScheduler scheduler = new WallpaperScheduler(this.activity);
        if (checked) {
            if (getFrequencyWallpaper() < 15) {
                view.setSwitchWallpaper(false);
                view.alertFrequencyError();
            } else {
                // TODO manage permission refused and toggle siwtch off
                Exec exec = new Exec() {
                    @Override
                    public void exec() {
                        ApplicationContext appContext = ApplicationContext.getInstance(activity);
                        scheduler.schedule(
                                getFolderHuman(),
                                getFrequencyWallpaper(),
                                getFrequencySyncMinute(), getAlbum(), getQuantity(), getRename(),
                                getFrequencyUpdatePhotosHour(), appContext);
                        view.enableFrequencyWallpaper(checked);
                        view.enableFrequencySync(checked);
                        view.enableFrequencyUpdatePhotos(checked);
                    }
                };
                Require require = SignInGoogleAPIWriteRequirementBuilder.build(exec, auth, view);
                permissionHandler.startRequirement(require);
            }
        } else {
            scheduler.cancel();
            view.enableFrequencyWallpaper(checked);
            view.enableFrequencySync(checked);
            view.enableFrequencyUpdatePhotos(checked);
        }
    }

    @Override
    public void onButtonSyncOnce() {
        Log.d(TAG, "onSyncClick");
        String album = this.album;
        if (album == null || album.equals("")) {
            view.alertNoAlbum();
        } else {
            SyncTask task = new SyncTask(this, getSync());
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
                // TODO call it from observer
                // refreshLastTime();
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

}

