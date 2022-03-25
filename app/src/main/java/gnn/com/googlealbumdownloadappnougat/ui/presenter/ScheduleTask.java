package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInGoogleAPIWriteRequirementBuilder;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

class ScheduleTask {

    public ScheduleTask(MainActivity activity, Context context, WallpaperScheduler scheduler, IViewFrequencies view, UserModel userModel) {
        this.activity = activity;
        this.context = context;
        this.scheduler = scheduler;
        this.view = view;
        this.userModel = userModel;
        this.authManager = new AuthManager(activity);
    }

    private MainActivity activity;
    private Context context;
    private WallpaperScheduler scheduler;
    private IViewFrequencies view;
    private UserModel userModel;
    private final AuthManager authManager;

    void schedule(boolean checked, long frequencyWallpaperMinute, int frequencySyncMinute, long frequencyUpdatePhotosHour) {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                ApplicationContext appContext = ApplicationContext.getInstance(context);
                PersistPrefMain preferences = new PersistPrefMain(context);
                scheduler.schedule(
                        preferences.getPhotoPath(),
                        frequencyWallpaperMinute,
                        frequencySyncMinute, preferences.getAlbum(), preferences.getQuantity(), preferences.getRename(),
                        frequencyUpdatePhotosHour, appContext);
                view.enableFrequencyWallpaper(checked);
                view.enableFrequencySync(checked);
                view.enableFrequencyUpdatePhotos(checked);
            }
        };
        AuthManager auth = authManager;
        Require require = SignInGoogleAPIWriteRequirementBuilder.build(exec, auth, view, userModel);
        this.activity.getPermissionHandler().startRequirement(require);
    }
}
