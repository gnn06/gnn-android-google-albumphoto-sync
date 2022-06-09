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
    private Wizard wizard;

    public PresenterWizard(MainActivity activity, FragmentWizard view, ViewModelWizard viewModel) {
        this.view = view;
        this.activity = activity;
        persist = new PersistPrefMain(this.activity);
        viewModelWizard = viewModel;
        wizard = new Wizard(null, persist, null, null, activity);
    }

    // For test
    public PresenterWizard(MainActivity activity, FragmentWizard view, PersistPrefMain persist,
                           ViewModelWizard viewModel) {
        this.view = view;
        this.activity = activity;
        this.persist = persist;
        viewModelWizard = viewModel;
        this.wizard = new Wizard(null, persist, null, null, activity);
    }

    public PresenterWizard(MainActivity activity, FragmentWizard view, PersistPrefMain persist,
                           ViewModelWizard viewModel, Wizard wizard) {
        this.view = view;
        this.activity = activity;
        this.persist = persist;
        viewModelWizard = viewModel;
        this.wizard = wizard;
    }

    public void onShowWizard() {
        this.activity.makeVisible(true);
        WizardStep step = persist.restoreWizardStep();
        if (step == WizardStep.S11_FINISHED) {
            step = wizard.resetStep();
        }
        viewModelWizard.getLiveStep().setValue(step);
    }

    public void nextStep() {
        WizardStep step = wizard.shiftToNextStep();
        viewModelWizard.setStep(step);
        this.view.setExplanation(step);
        // TODO faudrait un observer
    }

    public void onStopWizard() {
        wizard.setActive(false);
        WizardStep step = wizard.stop();
        activity.makeVisible(false);
        viewModelWizard.setStep(step);
    }

    public void reset() {
        WizardStep step = wizard.resetStep();
        viewModelWizard.setStep(step);
        this.view.setExplanation(step);
    }

    public void onAppStart() {
        WizardStep step = wizard.getStep();
        viewModelWizard.setStep(step);
        if (step != WizardStep.S11_FINISHED) {
            activity.makeVisible(true);
        }
    }
}
