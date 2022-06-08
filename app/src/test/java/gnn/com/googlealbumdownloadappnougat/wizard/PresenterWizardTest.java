package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

public class PresenterWizardTest {

    private PresenterWizard presenter;
    private PersistPrefMain persist;
    private FragmentWizard view;
    private MainActivity activity;
    private ViewModelWizard viewModel;
    private MutableLiveData<WizardStep> liveData;

    @Before
    public void setUp() throws Exception {
        this.persist = mock(PersistPrefMain.class);
        this.view = mock(FragmentWizard.class);
        this.activity = mock(MainActivity.class);
        liveData = mock(MutableLiveData.class);
        viewModel = mock(ViewModelWizard.class);
        when(viewModel.getLiveStep()).thenReturn(liveData);
        presenter = new PresenterWizard(activity, view, this.persist, viewModel);
    }

    @Test
    public void wizardNeverShown_AppStart_wizardShown() {
        // when app start, and wizard was never started, check that wizard is shown
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S00_NOT_STARTED);
        // when
        presenter.onAppStart();
        // then
        verify(activity).makeVisible(true);
    }

    @Test
    public void step01_AppStart_wizardShown() {
        // when app start, and wizard was never started, check that wizard is shown
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S01_LOGIN_AND_AUTHORISE);
        // when
        presenter.onAppStart();
        // then
        verify(activity).makeVisible(true);
    }

    @Test
    public void wizardAlreadyShown_AppStart_wizardNotShown() {
        // when aap starts, and state finish, then no wizard shown
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S11_FINISHED);
        // when
        presenter.onAppStart();
        // then
        verify(activity, never()).makeVisible(anyBoolean());
    }

    @Test
    public void wizardStopped_stopFinish() {
        // when call stop, persist finish state
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S06_ACTIVATE_LIVEWALLPAPER);
        // when
        presenter.onStopWizard();
        // then
        verify(persist).saveWizardStep(WizardStep.S11_FINISHED);
        verify(activity).makeVisible(false);
    }

    @Test
    public void reset() {
        // when
        presenter.reset();
        // then
        verify(persist).saveWizardStep(WizardStep.S00_NOT_STARTED);
        verify(view).setExplanation(WizardStep.S00_NOT_STARTED);
    }

    @Test
    public void wizardMenu_wizardShown_explanationAsStep() {
        // when show wizard,
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S06_ACTIVATE_LIVEWALLPAPER);
        when(viewModel.getLiveStep()).thenReturn(liveData);

        // when
        presenter.onShowWizard();
        // then
        verify(activity).makeVisible(true);
    }

    @Test
    public void wizardMenu_finishStep_wizardShown_explanationAsStep() {
        // when show wizard, and ste=FINISH, when restart wizard
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S11_FINISHED);
        // when
        presenter.onShowWizard();
        // then
        verify(activity).makeVisible(true);
    }

    @Test
    public void next() {
        // when next called, increment step
        // given
        when(persist.restoreWizardStep()).thenReturn(WizardStep.S06_ACTIVATE_LIVEWALLPAPER);
        // when
        presenter.nextStep();
        // then
        verify(persist).saveWizardStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY);
        verify(view).setExplanation(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY);
    }
}