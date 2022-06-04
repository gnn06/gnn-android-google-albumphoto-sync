package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import androidx.fragment.app.Fragment;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizard {

    public ViewWizard() {
    }

    int getViewFromStep(WizardStep step, Fragment fragment) {
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
                if (fragment instanceof FragmentHome) {
                    return R.id.sectionFrequencies;
                } else {
                    return R.id.SectionFreqeuncyWallpaper;
                }
            default:
                return -1;
        }
    }

    public void highlight(FragmentHighlight fragment) {
        int id = getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, fragment);
        fragment.highlightStepWizard(id, true);
    }
}
