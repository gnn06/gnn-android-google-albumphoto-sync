package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.Wizard;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizardTest {

    @Test
    public void getId_fragment_home() {
        // given
        ViewWizard viewWizard = new ViewWizard(null);
        // when
        int id = viewWizard.getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, new FragmentHome());
        // then
        assertThat(id, is(R.id.sectionFrequencies));
    }

    @Test
    public void getId_fragment_frequencies() {
        // given
        ViewWizard viewWizard = new ViewWizard(null);
        // when
        int id = viewWizard.getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, null);
        // then
        assertThat(id, is(R.id.SectionFreqeuncyWallpaper));
    }

    @Test
    public void highlight_call_view() {
        // given
        FragmentHighlight fragment = mock(FragmentHighlight.class);
        Wizard wizard = mock(Wizard.class);
        when(wizard.getNextStep()).thenReturn(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY);
        // Unit under test
        ViewWizard viewWizard = new ViewWizard(wizard);
        // when
        viewWizard.highlight(fragment);
        // then
        verify(fragment).highlightStepWizard(R.id.SectionFreqeuncyWallpaper, true);
    }
}