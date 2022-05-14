package gnn.com.googlealbumdownloadappnougat.wizard;

import android.view.View;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizard {

    final private FragmentWizard view;
    final private MainActivity activity;
    private final PersistPrefMain persist;

    public PresenterWizard(MainActivity activity, FragmentWizard view) {
        this.view = view;
        this.activity = activity;
        persist = new PersistPrefMain(this.activity);
    }

    public PresenterWizard(MainActivity activity, FragmentWizard view, PersistPrefMain persist) {
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
        this.activity.makeVisible(true);
        WizardStep step = persist.restoreWizardStep();
        if (step == WizardStep.S11_FINISHED) {
            Wizard wizard = new Wizard(null, persist, null, null, activity);
            step = wizard.resetStep();
        }
        if (this.view != null)
            this.view.setExplaination(step.ordinal());

        // TODO if already inflate, miss a setExplanation
    }

    public void nextStep() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.shiftToNextStep();
        this.view.setExplaination(step.ordinal());
    }

    public void onStopWizard() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        wizard.setActive(false);
        WizardStep step = wizard.stop();
        activity.makeVisible(false);
    }

    public void reset() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.resetStep();
        this.view.setExplaination(step.ordinal());
    }

    public void onAppStart() {

    }

}
