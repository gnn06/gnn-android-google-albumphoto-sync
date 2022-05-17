package gnn.com.googlealbumdownloadappnougat.wizard;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizard {

    final private FragmentWizard view;
    final private MainActivity activity;
    private final PersistPrefMain persist;

    public PresenterWizard(MainActivity activity, FragmentWizard view) {
        this.view = view;
        // TODO try to retrieve activity from fragment
        this.activity = activity;
        persist = new PersistPrefMain(this.activity);
    }

    public PresenterWizard(MainActivity activity, FragmentWizard view, PersistPrefMain persist) {
        this.view = view;
        this.activity = activity;
        this.persist = persist;
        
    }

    public void onShowWizard() {
        this.activity.makeVisible(true);
        WizardStep step = persist.restoreWizardStep();
        if (step == WizardStep.S11_FINISHED) {
            Wizard wizard = new Wizard(null, persist, null, null, activity);
            step = wizard.resetStep();
        }
    }

    public void nextStep() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.shiftToNextStep();
        this.view.setExplanation(step);
        // TODO faudrait un observer
    }

    public void onStopWizard() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        wizard.setActive(false);
        wizard.stop();
        activity.makeVisible(false);
    }

    public void reset() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.resetStep();
        this.view.setExplanation(step);
    }

    public void onAppStart() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.getStep();
        if (step != WizardStep.S11_FINISHED) {
            activity.makeVisible(true);
        }
    }

}
