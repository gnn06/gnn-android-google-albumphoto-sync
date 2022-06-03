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
        int id = -1;
        switch (step) {
            case S01_LOGIN_AND_AUTHORISE:
                id = R.id.SectionUser;
            case S03_CHOOSE_ALBUM:
                id = R.id.SectionAlbum;
            case S05_CHOOSE_FOLDER_AND_ASK_PERMISSION:
                id = R.id.SectionFolder;
            case S06_ACTIVATE_LIVEWALLPAPER:
                id = R.id.warning_wallpaper_active;
            case S07_CHOOSE_WALLPAPER_FREQUENCY:
//                    id = R.id.sectionFrequencies;
                    id = R.id.SectionFreqeuncyWallpaper;
        }
        View view = id != -1 ? fragment.findViewById(id) : null;
        return view;
    }
}
