package gnn.com.googlealbumdownloadappnougat.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import gnn.com.googlealbumdownloadappnougat.R;

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
        this.presenter = new PresenterWizard(this.getActivity(), this);
        getView().findViewById(R.id.button_wizard_next).setOnClickListener(v -> {
            presenter.nextStep();
        });
        getView().findViewById(R.id.button_wizard_reset).setOnClickListener(v -> {
            presenter.reset();
        });
        getView().findViewById(R.id.button_wizard_stop).setOnClickListener(v -> {
            presenter.stop();
        });
        presenter.onViewCreated();
    }

    public void setExplaination(int indice) {
        TextView stepView = getView().findViewById(R.id.text_wizard_step);
        String stringStep = getActivity().getResources().getString(R.string.text_wizard_step, indice + 1, WizardStep.values().length - 2);
        stepView.setText(stringStep);

        String[] strings = getActivity().getResources().getStringArray(R.array.wizard_explaination);
        TextView view = getView().findViewById(R.id.text_wizard_explaination);
        view.setText(strings[indice]);
    }
}
