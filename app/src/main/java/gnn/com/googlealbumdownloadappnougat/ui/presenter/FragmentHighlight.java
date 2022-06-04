package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.view.View;

import androidx.fragment.app.Fragment;

import gnn.com.googlealbumdownloadappnougat.R;

public class FragmentHighlight extends Fragment {

    private final ViewWizard viewWizard;

    public FragmentHighlight(int fragment_home) {
        super(fragment_home);
        viewWizard = new ViewWizard();
    }

    // For test
    public FragmentHighlight(ViewWizard viewWizard) {
        this.viewWizard = viewWizard;
    }

    public void highlightStepWizard(int id, boolean highlight) {
        if (id != -1) {
            View view = this.getView().findViewById(id);
            view.setBackgroundResource(highlight ? R.drawable.border : 0);
        }
    }

}
