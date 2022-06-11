package gnn.com.googlealbumdownloadappnougat.wizard;

import android.content.Context;

//import androidx.fragment.app.testing.FragmentScenario;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

@RunWith(RobolectricTestRunner.class)
public class PresenterWizardIntTest {

    private MainActivity activity;
    private FragmentWizard viewFragment;
    private PersistPrefMain persist;
    private ViewModelWizard viewModel;
    private Wizard wizard;

    private PresenterWizard presenterWizard;

    @Test
    public void name() {
        Context context = ApplicationProvider.getApplicationContext();
        this.persist = new PersistPrefMain(context);
        this.viewModel = new ViewModelWizard();
        this.viewModel.getLiveStep().observeForever(new Observer<WizardStep>() {
            @Override
            public void onChanged(WizardStep wizardStep) {
                assert true;
            }
        });
        this.wizard = new Wizard(null, persist, null, null, context);
//        ActivityScenario<MainActivity> launch = ActivityScenario.launch(MainActivity.class);
//        launch.moveToState(Lifecycle.State.CREATED);
//        launch.onActivity(activity1 -> {
//            this.activity = activity1;
//
//        });
        FragmentScenario<FragmentWizard> scenario = FragmentScenario.launchInContainer(FragmentWizard.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onFragment(fragment1 -> {
            PresenterWizard presenter = new PresenterWizard(activity, fragment1, persist, viewModel, wizard);
            presenter.nextStep();
            System.out.println("goi");
        });

    }
}
