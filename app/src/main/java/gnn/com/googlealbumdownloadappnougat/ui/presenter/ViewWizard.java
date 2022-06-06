package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.Wizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizard {

    private final Wizard wizard;

    public ViewWizard(PersistPrefMain persist, Context context) {
        this.wizard = new Wizard(null, persist, null, null, context);
    }

    public ViewWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    int getViewFromStep(WizardStep step, Fragment fragment) {
        if (fragment instanceof FragmentHome) {
            switch (step) {
                case S01_LOGIN_AND_AUTHORISE:
                    return R.id.SectionUser;
                case S03_CHOOSE_ALBUM:
                    return R.id.SectionAlbum;
                case S05_CHOOSE_FOLDER_AND_ASK_PERMISSION:
                    return R.id.SectionFolder;
                case S06_ACTIVATE_LIVEWALLPAPER:
                    return R.id.warning_wallpaper_active;
                case S07_CHOOSE_WALLPAPER_FREQUENCY:
                case S08_CHOOSE_DOWNLOAD_FREQUENCY:
                case S09_CHOOSE_UPDATE_FREQUENCY:
                case S10_ACTIVATE_SCHEDULER:
                    return R.id.sectionFrequencies;
                default:
                    return -1;
            }
        } else {
            switch (step) {
                case S07_CHOOSE_WALLPAPER_FREQUENCY:
                    return R.id.SectionFreqeuncyWallpaper;
                case S08_CHOOSE_DOWNLOAD_FREQUENCY:
                    return R.id.SectionFreqeuncySync;
                case S09_CHOOSE_UPDATE_FREQUENCY:
                    return R.id.SectionFreqeuncyUpdatePhotos;
                case S10_ACTIVATE_SCHEDULER:
                    return R.id.SectionActiveWallpaper;
                default:
                    return -1;
            }
        }
    }

    public void highlight(FragmentHighlight fragment) {
        WizardStep step = wizard.getNextStep();
        int id = getViewFromStep(step, fragment);
        fragment.highlightStepWizard(id, true);
    }

    public LiveData<WizardStep> getLiveStep() {
        return new MutableLiveData();
    }
}
