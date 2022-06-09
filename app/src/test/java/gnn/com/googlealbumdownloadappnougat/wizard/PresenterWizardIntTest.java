package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.mockito.Mockito.mock;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

@RunWith(AndroidJUnit4);
public class PresenterWizardIntTest {

    private MainActivity activity;
    private FragmentWizard viewFragment;
    private PersistPrefMain persist;
    private ViewModelWizard viewModel;
    private Wizard wizard;

    private PresenterWizard presenterWizard;

    @Before
    public void setUp() throws Exception {
        this.activity = mock(MainActivity.class);
        this.viewFragment = mock(FragmentWizard.class);
        this.persist = mock(PersistPrefMain.class);
        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.moveToState(Lifecycle.State.CREATED);
        this.viewModel = mock(ViewModelWizard.class);
//        this.viewModel = new ViewModelProvider(context).get(ViewModelWizard.class);
        this.wizard = mock(Wizard.class);
        presenterWizard = new PresenterWizard(activity, viewFragment, persist, viewModel, wizard);
    }

    @Test
    public void name() {
        // when
        presenterWizard.nextStep();

    }
}
