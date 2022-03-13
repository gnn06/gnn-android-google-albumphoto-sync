package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.Exec;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.auth.SignInGoogleAPIWriteRequirementBuilder;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.SynchronizerDelayed;

public class PresenterFrequencies implements IPresenterFrequencies {

    final private IViewFrequencies view;
    final private Context context;
    private MainActivity activity;
    private final UserModel userModel;

    public PresenterFrequencies(IViewFrequencies view, Context context, MainActivity activity) {
        this.view = view;
        this.context = context;
        this.activity = activity;
        userModel = new ViewModelProvider(activity).get(UserModel.class);
    }

    @Override
    public void onAppStart() {
        WallpaperScheduler scheduler = new WallpaperScheduler(this.context);
        boolean scheduled = scheduler.isScheduled();
        view.setSwitchWallpaper(scheduled);
        view.enableFrequencyWallpaper(scheduled);
        view.enableFrequencySync(scheduled);
        view.enableFrequencyUpdatePhotos(scheduled);
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
     *
     * @param frequency in days
     */
    @Override
    public void setFrequencyUpdatePhotos(int frequency) {
        view.setFrequencyUpdatePhotos(frequency == -1 ? "" : Integer.toString(frequency));
    }

    /**
     *
     * int frequencySync fréquence de téléchargement en minute
     */
    @Override
    public int getFrequencySyncMinute() {
        return getFrequencySync() * 60;
    }

    @Override
    public void onSwitchWallpaper(boolean checked) {
        WallpaperScheduler scheduler = new WallpaperScheduler(this.context);
        if (checked) {
            if (getFrequencyWallpaper() < 15) {
                view.setSwitchWallpaper(false);
                view.alertFrequencyError();
            } else {
                // TODO manage permission refused and toggle siwtch off
                Exec exec = new Exec() {
                    @Override
                    public void exec() {
                        ApplicationContext appContext = ApplicationContext.getInstance(context);
                        PersistPrefMain preferences = new PersistPrefMain(context);
                        scheduler.schedule(
                                preferences.getPhotoPath(),
                                getFrequencyWallpaper(),
                                getFrequencySyncMinute(), preferences.getAlbum(), preferences.getQuantity(), preferences.getRename(),
                                preferences.getFrequencyUpdatePhotosHour(), appContext);
                        view.enableFrequencyWallpaper(checked);
                        view.enableFrequencySync(checked);
                        view.enableFrequencyUpdatePhotos(checked);
                    }
                };
                AuthManager auth = new AuthManager(this.activity);
                Require require = SignInGoogleAPIWriteRequirementBuilder.build(exec, auth, view, userModel);
                activity.getPermissionHandler().startRequirement(require);
            }
        } else {
            scheduler.cancel();
            view.enableFrequencyWallpaper(checked);
            view.enableFrequencySync(checked);
            view.enableFrequencyUpdatePhotos(checked);
        }
    }


}
