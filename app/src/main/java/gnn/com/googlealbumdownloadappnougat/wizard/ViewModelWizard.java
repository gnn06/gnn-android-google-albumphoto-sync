package gnn.com.googlealbumdownloadappnougat.wizard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewModelWizard extends ViewModel {

    private final MutableLiveData<WizardStep> liveStep = new MutableLiveData<>();

    public MutableLiveData<WizardStep> getLiveStep() {
        return liveStep;
    }
}
