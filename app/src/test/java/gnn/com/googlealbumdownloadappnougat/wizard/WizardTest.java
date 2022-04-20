package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.wallpaper.MyWallpaperService;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

public class WizardTest {

    private AuthManager authManager;
    private PersistPrefMain persistPrefMain;
    private MyWallpaperService wallpaperService;
    private WallpaperScheduler wallpaperScheduler;

    @Before
    public void setUp() throws Exception {
        this.authManager = mock(AuthManager.class);
        this.persistPrefMain = mock(PersistPrefMain.class);
        this.wallpaperService = mock(MyWallpaperService.class);
        this.wallpaperScheduler = mock(WallpaperScheduler.class);
    }

    @Test
    public void persist_save_step() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        wizard.setStep(WizardStep.S02_ASK_GOOGLE_PERMISSION);
        // then
        verify(persistPrefMain).saveWizardStep(WizardStep.S02_ASK_GOOGLE_PERMISSION);
    }

    @Test
    public void persist_restore_step() {
        // Given
        Wizard wizard = new Wizard(this.authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        when(persistPrefMain.restoreWizardStep()).thenReturn(WizardStep.S03_CHOOSE_ALBUM);
        // when
        WizardStep step = wizard.getStep();
        // then
        assertThat(step, Is.is(WizardStep.S03_CHOOSE_ALBUM));
    }
}
