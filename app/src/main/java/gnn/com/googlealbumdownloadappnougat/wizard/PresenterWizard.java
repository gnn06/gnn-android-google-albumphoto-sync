package gnn.com.googlealbumdownloadappnougat.wizard;

import android.app.Activity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizard {

    final private FragmentWizard view;
    final private Activity activity;
    private final PersistPrefMain persist;

    public PresenterWizard(Activity activity, FragmentWizard view) {
        this.view = view;
        this.activity = activity;
        persist = new PersistPrefMain(this.activity);
    }

    public void switchToApp() {
        NavController controller = Navigation.findNavController(this.activity, R.id.fragment_container_view);
        controller.navigate(R.id.action_fragmentWizard_to_fragmentHome);
    }

    public void switchToWizard() {
        NavController controller = Navigation.findNavController(this.activity, R.id.fragment_container_view);
        controller.navigate(R.id.action_fragmentHome_to_fragmentWizard);
    }

    public void nextStep() {
        Wizard wizard = new Wizard(null, persist, null, null);
        wizard.shiftToNextStep();
        switchToWizard();
    }

    public void onViewCreated() {
        Wizard wizard = new Wizard(null, persist, null, null);
        WizardStep step = wizard.getStep();
        this.view.setExplaination(step.ordinal());
    }

    public void reset() {
        Wizard wizard = new Wizard(null, persist, null, null);
        wizard.resetStep();
    }
}
