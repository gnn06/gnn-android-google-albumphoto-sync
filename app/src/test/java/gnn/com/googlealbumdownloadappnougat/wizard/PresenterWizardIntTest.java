package gnn.com.googlealbumdownloadappnougat.wizard;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.ViewWizard;

@RunWith(RobolectricTestRunner.class)
public class PresenterWizardIntTest {

    private PersistPrefMain persist;
    private Wizard wizard;
    private PresenterWizard presenterWizard;

    @Test
    public void name() {
        Context context = ApplicationProvider.getApplicationContext();
        this.persist = new PersistPrefMain(context);
        this.wizard = new Wizard(null, persist, null, null, context);

        ActivityScenario<MainActivity> activityMock = ActivityScenario.launch(MainActivity.class);
        activityMock.moveToState(Lifecycle.State.STARTED);
        activityMock.onActivity(activity1 -> {
            ViewModelWizard viewModelWizard = new ViewModelProvider(activity1).get(ViewModelWizard.class);
            FragmentScenario<FragmentWizard> scenario = FragmentScenario.launchInContainer(FragmentWizard.class);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onFragment(viewWizard -> {
                PresenterWizard presenter = new PresenterWizard(activity1, viewWizard, persist, viewModelWizard, wizard);
                presenter.nextStep();
                presenter.nextStep();
                presenter.nextStep();
                System.out.println("goi");
            });
        });
    }
}
