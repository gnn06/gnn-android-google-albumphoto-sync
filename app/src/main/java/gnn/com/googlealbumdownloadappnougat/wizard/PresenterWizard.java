package gnn.com.googlealbumdownloadappnougat.wizard;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizard {

    // La vue gérant le fragment
    // non null que qd le presenter est utilisé directement depuis le fragment pour les
    // commandes next.
    final private FragmentWizard view;
    // L'activité gérant le menu et rendant le wizard visible
    final private MainActivity activity;
    private final PersistPrefMain persist;
    private final ViewModelWizard viewModelWizard;

    public PresenterWizard(MainActivity activity, FragmentWizard view, ViewModelWizard viewModel) {
        this.view = view;
        this.activity = activity;
        persist = new PersistPrefMain(this.activity);
        viewModelWizard = viewModel;
    }

    // For test
    public PresenterWizard(MainActivity activity, FragmentWizard view, PersistPrefMain persist,
                           ViewModelWizard viewModel) {
        this.view = view;
        this.activity = activity;
        this.persist = persist;
        viewModelWizard = viewModel;
    }

    public void onShowWizard() {
        this.activity.makeVisible(true);
        WizardStep step = persist.restoreWizardStep();
        if (step == WizardStep.S11_FINISHED) {
            Wizard wizard = new Wizard(null, persist, null, null, activity);
            step = wizard.resetStep();
        }
        viewModelWizard.getLiveStep().setValue(step);
    }

    public void nextStep() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.shiftToNextStep();
        viewModelWizard.setStep(step);
        this.view.setExplanation(step);
        // TODO faudrait un observer
    }

    public void onStopWizard() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        wizard.setActive(false);
        WizardStep step = wizard.stop();
        activity.makeVisible(false);
        viewModelWizard.setStep(step);
    }

    public void reset() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.resetStep();
        viewModelWizard.setStep(step);
        this.view.setExplanation(step);
    }

    public void onAppStart() {
        Wizard wizard = new Wizard(null, persist, null, null, activity);
        WizardStep step = wizard.getStep();
        viewModelWizard.setStep(step);
        if (step != WizardStep.S11_FINISHED) {
            activity.makeVisible(true);
        }
    }
}
