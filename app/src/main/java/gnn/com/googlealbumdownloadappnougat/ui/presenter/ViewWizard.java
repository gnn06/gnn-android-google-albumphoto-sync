package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizard {

    private final Fragment fragment;

    public ViewWizard(Fragment fragment) {
        this.fragment = fragment;
    }

    public View getViewFromStep(WizardStep step) {
        View view = fragment.getView();
        switch (step) {
            case S01_LOGIN_AND_AUTHORISE:
                return view.findViewById(R.id.SectionUser);
            case S03_CHOOSE_ALBUM:
                return view.findViewById(R.id.SectionAlbum);
            case S05_CHOOSE_FOLDER_AND_ASK_PERMISSION:
                return view.findViewById(R.id.SectionFolder);
            case S06_ACTIVATE_LIVEWALLPAPER:
                return view.findViewById(R.id.warning_wallpaper_active);
            case S07_CHOOSE_WALLPAPER_FREQUENCY:
                if (fragment instanceof FragmentHome) {
                    return view.findViewById(R.id.sectionFrequencies);
                } else {
                    return view.findViewById(R.id.SectionFreqeuncyWallpaper);
                }
            default:
                return null;
        }
    }
}
