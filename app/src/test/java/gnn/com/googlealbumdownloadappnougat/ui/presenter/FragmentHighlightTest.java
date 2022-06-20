package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

public class FragmentHighlightTest {
    @Test
    public void highlight_for_no_view() {
        // given
        ViewWizard viewWizard = mock(ViewWizard.class);
        when(viewWizard.getViewFromStep(any(), any())).thenReturn(-1);
        FragmentHighlight fragment = new FragmentHighlight(viewWizard);
        // then
        fragment.highlightStepWizard(-1, true);
    }
}