package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.ViewModelWizard;
import gnn.com.googlealbumdownloadappnougat.wizard.Wizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizardTest {

    private Wizard wizard;
    private ViewModelWizard viewModel;

    @Before
    public void setUp() throws Exception {
        wizard = mock(Wizard.class);
        viewModel = mock(ViewModelWizard.class);
    }

    @Test
    public void getId_frequency_fragmentHome() {
        // given
        ViewWizard viewWizard = new ViewWizard( null, viewModel);
        // when
        int id = viewWizard.getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, new FragmentHome());
        // then
        assertThat(id, is(-1));
        // when
        id = viewWizard.getViewFromStep(WizardStep.S08_CHOOSE_DOWNLOAD_FREQUENCY, new FragmentHome());
        // then
        assertThat(id, is(-1));
        // when
        id = viewWizard.getViewFromStep(WizardStep.S09_CHOOSE_UPDATE_FREQUENCY, new FragmentHome());
        // then
        assertThat(id, is(-1));
    }

    @Test
    public void getId_fragment_frequencies() {
        // given
        ViewWizard viewWizard = new ViewWizard( null, viewModel);
        // when
        int id = viewWizard.getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, null);
        // then
        assertThat(id, is(R.id.SectionFreqeuncyWallpaper));
    }

    @Test
    public void highlight_call_view() {
        // given
        FragmentHighlight fragment = mock(FragmentHighlight.class);
        when(wizard.getNextStep()).thenReturn(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY);
        when(wizard.getNextStep(any())).thenReturn(WizardStep.S11_ACTIVATE_LIVEWALLPAPER);
        // Unit under test
        ViewWizard viewWizard = new ViewWizard(wizard, viewModel);
        // when
        viewWizard.highlight(fragment);
        // then
        verify(fragment).highlightStepWizard(R.id.SectionFreqeuncyWallpaper, true);
    }
}