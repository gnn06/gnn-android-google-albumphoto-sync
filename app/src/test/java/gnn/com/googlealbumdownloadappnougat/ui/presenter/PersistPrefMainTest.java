package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

@RunWith(RobolectricTestRunner.class)
public class PersistPrefMainTest {

    @Test
    public void restore_invalid_step() {
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(ApplicationProvider.getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("wizard_step", "S44").apply();

        PersistPrefMain persist = new PersistPrefMain(ApplicationProvider.getApplicationContext());
        WizardStep step = persist.restoreWizardStep();

        assertThat(step, is(PersistPrefMain.DEF_WIZARD_STEP));
    }
}