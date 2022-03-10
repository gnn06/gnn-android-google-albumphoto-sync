package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;
import android.content.Intent;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.GoogleAuthRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.service.ActivitySchedule;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;
import gnn.com.photos.service.CacheManager;

public class PresenterMain implements IPresenterMain {

    private final AuthManager auth;
    private final IView view;
    private final UserModel userModel;
    private final PermissionHandler permissionHandler;
    private final Context activity;
    private final ApplicationContext applicationContext;
    private final PresenterHome presenterHome;

    public PresenterMain(AuthManager auth, IView view, UserModel userModel, PermissionHandler permissionHandler, Context activity, PresenterHome presenterHome) {
        this.auth = auth;
        this.view = view;
        this.userModel = userModel;
        this.permissionHandler = permissionHandler;
        this.activity = activity;
        this.presenterHome = presenterHome;
        applicationContext = ApplicationContext.getInstance(this.activity);
    }

    @Override
    public void onSignOut() {
        auth.signOut();
        getSync().resetCache();
        userModel.getUser().setValue(null);
    }

    @Override
    public void onMenuResetCache() {
        new CacheManager(applicationContext.getProcessFolder()).resetAll();
    }

    @Override
    public void onMenuScheduleDetail() {
        Intent intent = new Intent(activity, ActivitySchedule.class);
        activity.startActivity(intent);
    }

    @Override
    public void onMenuRequestGooglePermission() {
        Require require = new GoogleAuthRequirement(null, auth, view, userModel);
        permissionHandler.startRequirement(require);
    }

    @Override
    public void onAppStart() {

    }

    @Override
    public void onAppForeground() {

    }

    public SynchronizerAndroid getSync() {
        PersistPrefMain persistPref = new PersistPrefMain(this.activity);
        return new SynchronizerAndroid(activity, applicationContext.getCacheFile(), persistPref.getFrequencyUpdatePhotosHour(), applicationContext.getProcessFolder());
    }

}
