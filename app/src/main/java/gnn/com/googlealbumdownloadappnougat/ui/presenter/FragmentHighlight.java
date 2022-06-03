package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.view.View;

import androidx.fragment.app.Fragment;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class FragmentHighlight extends Fragment {

    public FragmentHighlight(int fragment_home) {
        super(fragment_home);
    }

    public void highlightStepWizard() {
        boolean highlight = true;
        WizardStep step = WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY;
        View view = new ViewWizard(this).getViewFromStep(step);
        if (view != null) {
            view.setBackgroundResource(highlight ? R.drawable.border : 0);
        }
    }

}
