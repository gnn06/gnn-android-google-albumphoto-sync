package gnn.com.googlealbumdownloadappnougat.wizard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.hasBackground;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

@RunWith(RobolectricTestRunner.class)
public class PresenterWizardIntTest {

    private PersistPrefMain persist;
    private Wizard wizard;
    private Context context;

    @Test
    public void name() {
        this.context = ApplicationProvider.getApplicationContext();
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
                assertThat(viewModelWizard.getLiveStep().getValue(), is(WizardStep.S05_CHOOSE_FOLDER_AND_ASK_PERMISSION));
                Drawable background = activity1.findViewById(R.id.warning_wallpaper_active).getBackground();
                assertThat(background, is(R.drawable.border));
            });
        });
    }
}
