package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.ViewModelWizard;
import gnn.com.googlealbumdownloadappnougat.wizard.Wizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class FragmentHighlight extends Fragment {

    private ViewModelWizard modelWizard;

    public FragmentHighlight(int fragment_home) {
        super(fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modelWizard = new ViewModelProvider(getActivity()).get(ViewModelWizard.class);
        modelWizard.getLiveStep().observe(getActivity(), new Observer<WizardStep>() {
            @Override
            public void onChanged(WizardStep step) {
                ViewWizard viewWizard =
                        new ViewWizard(new PersistPrefMain(getContext()), getActivity());
                viewWizard.highlight(FragmentHighlight.this);
            }
        });
    }

    // For test
    public FragmentHighlight(ViewWizard viewWizard) {

    }

    public void highlightStepWizard(int id, boolean highlight) {
        if (id != -1) {
            View view = this.getView().findViewById(id);
            view.setBackgroundResource(highlight ? R.drawable.border : 0);
        }
    }

}
