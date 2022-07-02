package gnn.com.googlealbumdownloadappnougat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Service;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.googlealbumdownloadappnougat.wizard.ViewModelWizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void viwModelWizard_initialized() {
        // Given
        Context context = ApplicationProvider.getApplicationContext();
        new PersistPrefMain(context).saveWizardStep(WizardStep.S03_CHOOSE_ALBUM);
        WallpaperScheduler schedulerMock = mock(WallpaperScheduler.class);
        when(schedulerMock.isScheduled()).thenReturn(false);
        ServiceLocator.getInstance().setWallpaperScheduler(schedulerMock);
        // When
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            ViewModelWizard viewModel = new ViewModelProvider(activity).get(ViewModelWizard.class);
            // then
            WizardStep step = viewModel.getLiveStep().getValue();
            assertThat(step, is(WizardStep.S03_CHOOSE_ALBUM));
        });
    }
}