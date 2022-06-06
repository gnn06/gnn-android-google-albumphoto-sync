package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.Wizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class FragmentHighlight extends Fragment {

    private final ViewWizard viewWizard;

    public FragmentHighlight(int fragment_home) {
        super(fragment_home);
        viewWizard = new ViewWizard(new Wizard(null, new PersistPrefMain(getContext()), null, null, getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewWizard.getLiveStep().observe(getActivity(), new Observer<WizardStep>() {
            @Override
            public void onChanged(WizardStep step) {
                new ViewWizard(null, null).getViewFromStep(step, getParentFragment());
            }
        });
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
