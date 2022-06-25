package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

@RunWith(RobolectricTestRunner.class)
public class FragmentFrequenciesIntTest {

//    @Rule
//    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void test() {
        // Check Fragment with Presenter
        // mock schedule

        WallpaperScheduler schedulerMock = mock(WallpaperScheduler.class);
        when(schedulerMock.isScheduled()).thenReturn(true);
        ServiceLocator.getInstance().setWallpaperScheduler(schedulerMock);

        WallpaperSchedulerWithPermission taskMock = mock(WallpaperSchedulerWithPermission.class);
        ServiceLocator.getInstance().setSyncTask(taskMock);

        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launch(FragmentFrequencies.class);

        verify(taskMock, never()).schedule(anyLong(), anyInt(), anyLong());
        
        // in test, getActivity=FragmentActivity
        // in app, getActivity=Mainactivity
    }

    @Test
    public void toggle_with_no_permission() {
        WallpaperScheduler schedulerMock = mock(WallpaperScheduler.class);
        when(schedulerMock.isScheduled()).thenReturn(false);
        ServiceLocator.getInstance().setWallpaperScheduler(schedulerMock);

        WallpaperSchedulerWithPermission taskMock = mock(WallpaperSchedulerWithPermission.class);
        ServiceLocator.getInstance().setSyncTask(taskMock);

        // Need switch and not switchCompat to avoid nullpointer
        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launchInContainer(FragmentFrequencies.class);

        onView(withId(R.id.SectionFreqeuncyWallpaper)).perform(click());

        // TODO roblem if alert not visible, test goes in infinity loop
        onView(withText(R.string.pick_frequency)).inRoot(isDialog()).check(matches(isDisplayed()));

        System.out.println("done");
    }
}