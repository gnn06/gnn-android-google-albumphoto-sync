package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        wizard.setStep(WizardStep.S02_ASK_GOOGLE_PERMISSION);
        // then
        String value = preferences.getString("wizard_active", "AZE");
        assertThat(value, Is.is(WizardStep.S02_ASK_GOOGLE_PERMISSION.name()));
    }

    @Test
    public void persist_restore_step() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("wizard_active", WizardStep.S03_CHOOSE_ALBUM.name());
        edit.apply();
        // when
        WizardStep step = wizard.getStep();
        // then
        assertThat(step, Is.is(WizardStep.S03_CHOOSE_ALBUM));
    }
}
