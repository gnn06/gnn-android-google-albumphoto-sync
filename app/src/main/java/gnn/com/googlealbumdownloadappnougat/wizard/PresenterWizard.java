package gnn.com.googlealbumdownloadappnougat.wizard;

import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizard {

    final private FragmentWizard view;
    final private FragmentActivity activity;
    private final PersistPrefMain persist;

    public PresenterWizard(FragmentActivity activity, FragmentWizard view) {
        this.view = view;
        this.activity = activity;
        persist = new PersistPrefMain(this.activity);
    }

    public PresenterWizard(FragmentActivity activity, FragmentWizard view, PersistPrefMain persist) {
        this.view = view;
        this.activity = activity;
        this.persist = persist;
        
    }

//    public void switchToApp() {
//        NavController controller = Navigation.findNavController(this.activity, R.id.fragment_container_view);
//        controller.navigate(R.id.action_fragmentWizard_to_fragmentHome);
//    }

    public void onShowWizard() {
//        NavController controller = Navigation.findNavController(this.activity, R.id.fragment_container_view);
//        controller.navigate(R.id.action_fragmentHome_to_fragmentWizard);
        view.makeVisible(true);
    }

    public void nextStep() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.shiftToNextStep();
        this.view.setExplaination(step.ordinal());
    }

    public void onStopWizard() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        wizard.setActive(false);
        WizardStep step = wizard.resetStep();
        view.setExplaination(step.ordinal());
        activity.findViewById(R.id.fragment_container_wizard).setVisibility(View.GONE);
    }

    public void reset() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.resetStep();
        this.view.setExplaination(step.ordinal());
    }

    public void onAppStart() {

    }

    public void onViewCreated() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.getStep();
        this.view.setExplaination(step.ordinal());
    }
}
