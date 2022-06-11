package gnn.com.googlealbumdownloadappnougat.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class FragmentWizard extends Fragment {

    private PresenterWizard presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wizard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelWizard viewModel = new ViewModelProvider(getActivity()).get(ViewModelWizard.class);
        this.presenter = new PresenterWizard(/*(MainActivity)*/ getActivity(), this, viewModel);
        getView().findViewById(R.id.button_wizard_next).setOnClickListener(v -> {
            presenter.nextStep();
        });
        getView().findViewById(R.id.button_wizard_stop).setOnClickListener(v -> {
            presenter.onStopWizard();
        });
        getView().findViewById(R.id.button_wizard_reset).setOnClickListener(v -> {
            presenter.reset();
        });
    }

    public void setExplanation(WizardStep stepDone) {
        int index = stepDone.ordinal(); // 0 when step = S00_NOT_STARTED;
        if (stepDone == WizardStep.S11_FINISHED) {
            // if wizard is finished, use before last step
            index = WizardStep.values().length - 2;
        }
        TextView stepView = getView().findViewById(R.id.text_wizard_step);
        String stringStep = getActivity().getResources().getString(R.string.text_wizard_step, index + 1, WizardStep.values().length - 1);
        stepView.setText(stringStep);

        String[] strings = getActivity().getResources().getStringArray(R.array.wizard_explanation);
        TextView view = getView().findViewById(R.id.text_wizard_explaination);
        String explanation = strings[index];
        view.setText(explanation);
    }

    @Override
    public void onResume() {
        // called once on first
        super.onResume();
        PersistPrefMain persist = new PersistPrefMain(requireActivity());
        WizardStep step = persist.restoreWizardStep();
        setExplanation(step);
    }
}
