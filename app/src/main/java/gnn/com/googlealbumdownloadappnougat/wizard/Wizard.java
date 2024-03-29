package gnn.com.googlealbumdownloadappnougat.wizard;

import android.content.Context;

import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.wallpaper.MyWallpaperService;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class Wizard {

    private final PersistPrefMain persistPrefMain;

    public Wizard(AuthManager authManager, PersistPrefMain persistPrefMain, MyWallpaperService wallpaperService, WallpaperScheduler wallpaperScheduler, Context context) {
        this.persistPrefMain = persistPrefMain;
    }

//    public WizardStep getNextStepDynamic() {
//        if (!authManager.isSignIn()) {
//            return WizardStep.S01_LOGIN;
//        } else if (!authManager.hasGooglePermission()) {
//            return WizardStep.S02_ASK_GOOGLE_PERMISSION;
//        } else if (persistPrefMain.getAlbum() == null) {
//            return WizardStep.S03_CHOOSE_ALBUM;
//        } else if (!authManager.hasWritePermission()) {
//            return WizardStep.S04_ASK_WRITE_PERMISSION;
//        } else if (persistPrefMain.getPhotoPath() == null) {
//            return WizardStep.S05_CHOOSE_FOLDER;
//        } else if (!wallpaperService.isActive(context)) {
//            return WizardStep.S06_ACTIVATE_LIVEWALLPAPER;
//        } else if (persistPrefMain.getFrequencyWallpaper() == PersistPrefMain.DEF_FREQ_WALLPAPER_MINUTE) {
//            return WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY;
//        } else if (persistPrefMain.getFrequencyDownload() == PersistPrefMain.DEF_FREQ_SYNC_HOUR) {
//            return WizardStep.S08_CHOOSE_DOWNLOAD_FREQUENCY;
//        } else if (persistPrefMain.getFrequencyUpdatePhotosHour() == PersistPrefMain.DEF_FREQ_UPDATE_PHOTO_DAY) {
//            return WizardStep.S09_CHOOSE_UPDATE_FREQUENCY;
//        } else if (!wallpaperScheduler.isScheduled()) {
//            return WizardStep.S10_ACTIVATE_SCHEDULER;
//        } else
//            return WizardStep.S11_FINISHED;
//    }

    public void setStep(WizardStep step) {
        persistPrefMain.saveWizardStep(step);
    }

    public WizardStep getStep() {
        return persistPrefMain.restoreWizardStep();
    }

    public void setActive(boolean isActive) {
        persistPrefMain.saveWizardActive(isActive);
    }

    public boolean isActive() {
        return persistPrefMain.restoreWizardActive();
    }

    /**
     * @return current step + 1, Don't update persistence
     */
    public WizardStep getNextStep() {
        WizardStep step = getStep();
        return getNextStep(step);
    }

    public WizardStep getNextStep(WizardStep currentStep) {
        int ordinal = currentStep.ordinal();
        if (ordinal < WizardStep.values().length - 1) {
            return WizardStep.values()[ordinal + 1];
        } else {
            return currentStep;
        }
    }

    public WizardStep shiftToNextStep() {
        WizardStep step = getNextStep();
        setStep(step);
        return step;
    }

    public WizardStep resetStep() {
        WizardStep step = WizardStep.S00_NOT_STARTED;
        setStep(step);
        return step;
    }

    public WizardStep stop() {
        WizardStep step = WizardStep.S20_FINISHED;
        setStep(step);
        return step;
    }
}
