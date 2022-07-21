package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.wallpaper.MyWallpaperService;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import androidx.test.core.app.ApplicationProvider;

@RunWith(RobolectricTestRunner.class)
public class WizardTest {

    private AuthManager authManager;
    private PersistPrefMain persistPrefMain;
    private MyWallpaperService wallpaperService;
    private WallpaperScheduler wallpaperScheduler;
    private SharedPreferences preferences;

    @Before
    public void setUp() throws Exception {
        this.authManager = null;
        this.persistPrefMain = new PersistPrefMain(ApplicationProvider.getApplicationContext());
        this.wallpaperService = null;
        this.wallpaperScheduler = null;
        preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void persist_save_step() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler, null);
        // when
        wizard.setStep(WizardStep.S03_CHOOSE_ALBUM);
        // then
        String value = preferences.getString("wizard_step", "AZE");
        assertThat(value, Is.is(WizardStep.S03_CHOOSE_ALBUM.name()));
    }

    @Test
    public void persist_restore_step() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler, null);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("wizard_step", WizardStep.S03_CHOOSE_ALBUM.name());
        edit.apply();
        // when
        WizardStep step = wizard.getStep();
        // then
        assertThat(step, Is.is(WizardStep.S03_CHOOSE_ALBUM));
    }

    @Test
    public void persist_save_isActive() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler, null);
        // when
        wizard.setActive(true);
        // then
        boolean value = preferences.getBoolean("wizard_active", true);
        assertThat(value, Is.is(true));
    }

    @Test
    public void persist_restore_isActive() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler, null);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("wizard_active", true);
        edit.apply();
        // when
        boolean active = wizard.isActive();
        // then
        assertThat(active, Is.is(true));
    }

    @Test
    public void getNext_first() {
        Wizard wizard = new Wizard(null, null, null, null, null);
        WizardStep result = wizard.getNextStep(WizardStep.S00_NOT_STARTED);
        assertThat(result, Is.is(WizardStep.S01_LOGIN_AND_AUTHORISE));
    }

    @Test
    public void getNext_middle() {
        Wizard wizard = new Wizard(null, null, null, null, null);
        WizardStep result = wizard.getNextStep(WizardStep.S01_LOGIN_AND_AUTHORISE);
        assertThat(result, Is.is(WizardStep.S03_CHOOSE_ALBUM));
    }

    @Test
    public void getNext_last() {
        Wizard wizard = new Wizard(null, null, null, null, null);
        WizardStep result = wizard.getNextStep(WizardStep.S20_FINISHED);
        assertThat(result, Is.is(WizardStep.S20_FINISHED));
    }
}
