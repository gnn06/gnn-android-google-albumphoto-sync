package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;

public class PresenterFrequenciesTest {

    @Test
    public void getFrequencyUpdatePhotosHour() {
        IViewFrequencies view = mock(IViewFrequencies.class);
        Context context = null;
        MainActivity activity = null;
        UserModel usermodel = null;
        PresenterFrequencies presenter = new PresenterFrequencies(view, context, activity, usermodel);
        when(view.getFrequencyUpdatePhotos()).thenReturn("10");
        assertThat(presenter.getFrequencyUpdatePhotosHour(), is(10 * 24));
    }
}