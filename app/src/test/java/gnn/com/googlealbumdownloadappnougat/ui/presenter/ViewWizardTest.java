package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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


}