package gnn.com.googlealbumdownloadappnougat.wizard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelWizard extends ViewModel {

    private final MutableLiveData<WizardStep> liveStep = new MutableLiveData<>(WizardStep.S00_NOT_STARTED);

    private WizardStep previousStep = WizardStep.S00_NOT_STARTED;

    public MutableLiveData<WizardStep> getLiveStep() {
        return liveStep;
    }

    public void setStep(WizardStep step) {
        this.previousStep = this.liveStep.getValue();
        this.liveStep.setValue(step);
    }

    public WizardStep getPreviousStep() {
        return previousStep;
    }
}
