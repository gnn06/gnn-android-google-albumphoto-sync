package gnn.com.googlealbumdownloadappnougat.wizard;

import androidx.fragment.app.FragmentActivity;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizard {

    // La vue gérant le fragment
    // non null que qd le presenter est utilisé directement depuis le fragment pour les
    // commandes next.
    final private FragmentWizard view;
    // L'activité gérant le menu et rendant le wizard visible
    final private FragmentActivity activity;
    private final PersistPrefMain persist;
    private final ViewModelWizard viewModelWizard;
    private final Wizard wizard;

    public PresenterWizard(FragmentActivity activity, FragmentWizard view, ViewModelWizard viewModel) {
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


    /**
     * For test
     * @param view view to use to set explanation
     */
    public PresenterWizard(MainActivity activity, FragmentWizard view, PersistPrefMain persist,
                           ViewModelWizard viewModel, Wizard wizard) {
        this.view = view;
        this.activity = activity;
        this.persist = persist;
        viewModelWizard = viewModel;
        this.wizard = wizard;
    }

    public void onShowWizard() {
        ((MainActivity)this.activity).makeVisible(true);
        WizardStep step = persist.restoreWizardStep();
        if (step == WizardStep.S20_FINISHED) {
            step = wizard.resetStep();
        }
        viewModelWizard.getLiveStep().setValue(step);
    }

    public void nextStep() {
        WizardStep step = wizard.shiftToNextStep();
        viewModelWizard.setStep(step);
    }

    public void onStopWizard() {
        wizard.setActive(false);
        WizardStep step = wizard.stop();
        ((MainActivity)activity).makeVisible(false);
        viewModelWizard.setStep(step);
    }

    public void reset() {
        WizardStep step = wizard.resetStep();
        viewModelWizard.setStep(step);
    }

    public void onAppStart() {
        WizardStep step = wizard.getStep();
        viewModelWizard.setStep(step);
        ((MainActivity)activity).makeVisible(step != WizardStep.S20_FINISHED);
    }
}
