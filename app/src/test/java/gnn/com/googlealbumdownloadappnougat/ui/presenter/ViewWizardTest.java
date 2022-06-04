package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class ViewWizardTest {

    @Test
    public void fragment_home() {
        // given
        ViewWizard viewWizard = new ViewWizard();
        // when
        int id = viewWizard.getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, new FragmentHome());
        // then
        assertThat(id, is(R.id.sectionFrequencies));
    }

    @Test
    public void fragment_frequencies() {
        // given
        ViewWizard viewWizard = new ViewWizard();
        // when
        int id = viewWizard.getViewFromStep(WizardStep.S07_CHOOSE_WALLPAPER_FREQUENCY, null);
        // then
        assertThat(id, is(R.id.SectionFreqeuncyWallpaper));
    }

    @Test
    public void getStep() {
        // given
        ViewWizard viewWizard = new ViewWizard();
        FragmentHighlight fragment = mock(FragmentHighlight.class);
        // when
        viewWizard.highlight(fragment);
        // then
        verify(fragment).highlightStepWizard(anyInt(), anyBoolean());
    }
}