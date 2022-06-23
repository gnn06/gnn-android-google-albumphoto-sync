package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

@RunWith(RobolectricTestRunner.class)
public class FragmentFrequenciesIntTest {

    @Test
    public void test() {
        // Check Fragment with Presenter
        // mock schedule
        WallpaperScheduler schedulerMock = new WallpaperScheduler(ApplicationProvider.getApplicationContext()) {
            @Override
            public boolean isScheduled() {
                return true;
            }
        };
        ServiceLocator.getInstance().setWallpaperScheduler(schedulerMock);
        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launch(FragmentFrequencies.class);
        org.junit.Assert.assertTrue(true);
        // in test, getActivity=FragmentActivity
        // in app, getActivity=Mainactivity
    }
}