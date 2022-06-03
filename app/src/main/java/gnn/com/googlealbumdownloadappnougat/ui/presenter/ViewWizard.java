package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.view.View;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizard {

    private final View fragment;

    public ViewWizard(View fragment) {
        this.fragment = fragment;
    }

    public View getViewFromStep(WizardStep step) {
        switch (step) {
            case S01_LOGIN_AND_AUTHORISE:
                return fragment.findViewById(R.id.SectionUser);
            case S03_CHOOSE_ALBUM:
                return fragment.findViewById(R.id.SectionAlbum);
            case S05_CHOOSE_FOLDER_AND_ASK_PERMISSION:
                return fragment.findViewById(R.id.SectionFolder);
            case S06_ACTIVATE_LIVEWALLPAPER:
                return fragment.findViewById(R.id.warning_wallpaper_active);
            case S07_CHOOSE_WALLPAPER_FREQUENCY:
                if (fragment.findViewById(R.id.SectionFreqeuncyWallpaper) != null) {
                    return fragment.findViewById(R.id.SectionFreqeuncyWallpaper);
                } else {
                    return fragment.findViewById(R.id.sectionFrequencies);
                }
            default:
                return null;
        }
    }
}
