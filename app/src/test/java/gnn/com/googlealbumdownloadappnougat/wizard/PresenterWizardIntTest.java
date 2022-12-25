package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.Log;
import android.view.Gravity;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.transition.Slide;
import androidx.work.Configuration;
import androidx.work.testing.SynchronousExecutor;
import androidx.work.testing.WorkManagerTestInitHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;

@RunWith(RobolectricTestRunner.class)
public class PresenterWizardIntTest {

    private PersistPrefMain persist;
    private Wizard wizard;
    private Context context;

    @Before
    public void setUp() throws Exception {
        this.context = ApplicationProvider.getApplicationContext();
        // Configure Worker because Presenter.onAppStart use WorkManager
        final Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config);
    }

    @Test
    public void name() {
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
                // normal = ripple, warning = colorDraw, border = Gradient
                assertTrue(activity1.findViewById(R.id.SectionFolder).getBackground() instanceof RippleDrawable);
                presenter.nextStep();
                presenter.nextStep();
                assertThat(activity1.findViewById(R.id.SectionFolder).getBackground(), instanceOf(GradientDrawable.class));
                presenter.nextStep();
                // when assert fails, error is "main loop enqued message"
                assertThat(activity1.findViewById(R.id.SectionFolder).getBackground(), nullValue());
//                assertThat(activity1.findViewById(R.id.warning_wallpaper_active).getBackground(), instanceOf(GradientDrawable.class));
//                assertThat(viewModelWizard.getLiveStep().getValue(), is(WizardStep.S05_CHOOSE_FOLDER_AND_ASK_PERMISSION));
//                presenter.onStopWizard();
//                assertTrue(activity1.findViewById(R.id.warning_wallpaper_active).getBackground() instanceof RippleDrawable);
            });
        });
    }
}
