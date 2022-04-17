package gnn.com.googlealbumdownloadappnougat.wizard;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void determine_step_00_none() {
        when(authManager.isSignIn()).thenReturn(false);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        WizardStep step = wizard.getNextStep();
        assertThat(step, is(WizardStep.S01_LOGIN));
    }

    @Test
    public void determine_step_01_is_login() {
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(false);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        WizardStep step = wizard.getNextStep();
        assertThat(step, is(WizardStep.S02_ASK_GOOGLE_PERMISSION));
    }

    @Test
    public void determine_step_02_has_google_permission() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn(null);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S03_CHOOSE_ALBUM));
    }

    @Test
    public void determine_step_03_album_choosed() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S04_ASK_WRITE_PERMISSION));
    }

    @Test
    public void determine_step_04_has_write_permission() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn(null);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S05_CHOOSE_FOLDER));
    }

    @Test
    public void determine_step_05_folder_choosed() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn("folder");
        when(wallpaperService.isActive()).thenReturn(false);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S06_ACTIVATE_LIVEWALLPAPER));
    }

    @Test
    public void determine_step_06_wallpaper_activated() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn("folder");
        when(wallpaperService.isActive()).thenReturn(true);
        when(persistPrefMain.getFrequencyWallpaper()).thenReturn(PersistPrefMain.DEF_FREQ_WALLPAPER_MINUTE);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY));
    }

    @Test
    public void determine_step_07_freq_wallpaper_choosed() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn("folder");
        when(wallpaperService.isActive()).thenReturn(true);
        when(persistPrefMain.getFrequencyWallpaper()).thenReturn(15);
        when(persistPrefMain.getFrequencyDownload()).thenReturn(PersistPrefMain.DEF_FREQ_SYNC_HOUR);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S08_CHOOSE_DOWNLOAD_FREQUENCY));
    }

    @Test
    public void determine_step_08_freq_download_choosed() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn("folder");
        when(wallpaperService.isActive()).thenReturn(true);
        when(persistPrefMain.getFrequencyWallpaper()).thenReturn(15);
        when(persistPrefMain.getFrequencyDownload()).thenReturn(60);
        when(persistPrefMain.getFrequencyUpdatePhotosHour()).thenReturn(PersistPrefMain.DEF_FREQ_UPDATE_PHOTO_DAY);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S09_CHOOSE_UPDATE_FREQUENCY));
    }

    @Test
    public void determine_step_09_freq_update_choosed() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn("folder");
        when(wallpaperService.isActive()).thenReturn(true);
        when(persistPrefMain.getFrequencyWallpaper()).thenReturn(15);
        when(persistPrefMain.getFrequencyDownload()).thenReturn(60);
        when(persistPrefMain.getFrequencyUpdatePhotosHour()).thenReturn(60);
        when(wallpaperScheduler.isScheduled()).thenReturn(false);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S10_ACTIVATE_SCHEDULER));
    }

    @Test
    public void determine_step_10_all_steps_done() {
        // Given
        when(authManager.isSignIn()).thenReturn(true);
        when(authManager.hasGooglePermission()).thenReturn(true);
        when(persistPrefMain.getAlbum()).thenReturn("album");
        when(authManager.hasWritePermission()).thenReturn(true);
        when(persistPrefMain.getPhotoPath()).thenReturn("folder");
        when(wallpaperService.isActive()).thenReturn(true);
        when(persistPrefMain.getFrequencyWallpaper()).thenReturn(15);
        when(persistPrefMain.getFrequencyDownload()).thenReturn(60);
        when(persistPrefMain.getFrequencyUpdatePhotosHour()).thenReturn(60);
        when(wallpaperScheduler.isScheduled()).thenReturn(true);
        Wizard wizard = new Wizard(authManager, persistPrefMain, wallpaperService, wallpaperScheduler);
        // when
        WizardStep step = wizard.getNextStep();
        // then
        assertThat(step, is(WizardStep.S11_FINISHED));
    }
}