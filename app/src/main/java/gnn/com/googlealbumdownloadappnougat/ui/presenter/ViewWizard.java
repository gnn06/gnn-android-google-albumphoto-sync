package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.ViewModelWizard;
import gnn.com.googlealbumdownloadappnougat.wizard.Wizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizard {

    public static final int NO_HIGHLIGHT = -1;
    private final Wizard wizard;
    private final ViewModelWizard viewModel;

    public ViewWizard(PersistPrefMain persist, FragmentActivity context) {
        this.wizard = new Wizard(null, persist, null, null, context);
        this.viewModel = new ViewModelProvider(context).get(ViewModelWizard.class);
    }

    // For test
    public ViewWizard(Wizard wizard, ViewModelWizard viewModel) {
        this.wizard = wizard;
        this.viewModel = viewModel;
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
                case S05BIS_QUANTITY:
                    return R.id.sectionDownloadOptions;
                case S11_ACTIVATE_LIVEWALLPAPER:
                    return NO_HIGHLIGHT;
                case S07_CHOOSE_WALLPAPER_FREQUENCY:
                case S08_CHOOSE_DOWNLOAD_FREQUENCY:
                case S09_CHOOSE_UPDATE_FREQUENCY:
                    return NO_HIGHLIGHT;
                case S10_SYNC_ONCE:
                    return R.id.SectionSync;
                default:
                    return NO_HIGHLIGHT;
            }
        } else {
            // FragmentFrequency
            switch (step) {
                case S07_CHOOSE_WALLPAPER_FREQUENCY:
                    return R.id.SectionFreqeuncyWallpaper;
                case S08_CHOOSE_DOWNLOAD_FREQUENCY:
                    return R.id.SectionFreqeuncySync;
                case S09_CHOOSE_UPDATE_FREQUENCY:
                    return R.id.SectionFreqeuncyUpdatePhotos;
                default:
                    return NO_HIGHLIGHT;
            }
        }
    }

    public void highlight(FragmentHighlight fragment) {
        WizardStep currentStep = wizard.getNextStep(viewModel.getPreviousStep());
        int id = getViewFromStep(currentStep, fragment);
        fragment.highlightStepWizard(id, false);
        WizardStep nextStep = wizard.getNextStep();
        id = getViewFromStep(nextStep, fragment);
        fragment.highlightStepWizard(id, true);
    }
}
