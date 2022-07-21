package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.service.SyncScheduler;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;

@RunWith(RobolectricTestRunner.class)
public class FragmentFrequenciesIntTest {

//    @Rule
//    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @After
    public void tearDown() throws Exception {
        ServiceLocator.getInstance().resetForTest();
    }

    @Test
    public void no_schedule_onAppStart() {
        // Check Fragment with Presenter
        // mock schedule

        WallpaperScheduler schedulerMock = mock(WallpaperScheduler.class);
        when(schedulerMock.isScheduled()).thenReturn(true);
        ServiceLocator.getInstance().setWallpaperScheduler(schedulerMock);

        WallpaperSchedulerWithPermission taskMock = mock(WallpaperSchedulerWithPermission.class);
        ServiceLocator.getInstance().setWallpaperSchedulerWithPermission(taskMock);

        PersistPrefMain persistMock = mock(PersistPrefMain.class);
        when(persistMock.getFrequencyWallpaper()).thenReturn(15);
        when(persistMock.getFrequencyDownload()).thenReturn(-1);
        when(persistMock.getFrequencyUpdatePhotosHour()).thenReturn(-1);
        ServiceLocator.getInstance().setPersistMain(persistMock);

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
        ServiceLocator.getInstance().setWallpaperSchedulerWithPermission(taskMock);

        PersistPrefMain persistMock = new PersistPrefMain(ApplicationProvider.getApplicationContext());
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(ApplicationProvider.getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("frequency_wallpaper", 15).apply();
        sharedPreferences.edit().putInt("frequency_sync", -1).apply();
        sharedPreferences.edit().putInt("frequency_update_photos", -1).apply();
        ServiceLocator.getInstance().setPersistMain(persistMock);

        // Need switch and not switchCompat to avoid nullpointer
        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launchInContainer(FragmentFrequencies.class);

        onView(withId(R.id.SectionFreqeuncyWallpaper)).perform(click());

        // TODO roblem if alert not visible, test goes in infinity loop
        onView(withText(R.string.pick_frequency)).inRoot(isDialog()).check(matches(isDisplayed()));

        System.out.println("done");
    }

    @Test
    public void change_freq_schedule() {
        WallpaperSchedulerWithPermission taskMock = mock(WallpaperSchedulerWithPermission.class);
        ServiceLocator.getInstance().setWallpaperSchedulerWithPermission(taskMock);

        SyncScheduler syncMock = mock(SyncScheduler.class);
        ServiceLocator.getInstance().setSyncScheduler(syncMock);

        PersistPrefMain persistMock = new PersistPrefMain(ApplicationProvider.getApplicationContext());
        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(ApplicationProvider.getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("frequency_wallpaper", -1).apply();
        sharedPreferences.edit().putInt("frequency_sync", -1).apply();
        sharedPreferences.edit().putInt("frequency_update_photos", -1).apply();

        ServiceLocator.getInstance().setPersistMain(persistMock);

        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launchInContainer(FragmentFrequencies.class);

        onView(withId(R.id.SectionFreqeuncyWallpaper)).perform(click());

        onData(anything())
                .inRoot(isDialog())
                .atPosition(1).perform(click());

        verify(taskMock).schedule(anyLong(), anyInt(), anyLong());
    }

    @Test
    public void change_freq_cancel() {
        WallpaperSchedulerWithPermission taskMock = mock(WallpaperSchedulerWithPermission.class);
        ServiceLocator.getInstance().setWallpaperSchedulerWithPermission(taskMock);

        SyncScheduler syncMock = mock(SyncScheduler.class);
        ServiceLocator.getInstance().setSyncScheduler(syncMock);

        SharedPreferences sharedPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(ApplicationProvider.getApplicationContext().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("frequency_wallpaper", 15).apply();
        sharedPreferences.edit().putInt("frequency_sync", -1).apply();
        sharedPreferences.edit().putInt("frequency_update_photos", -1).apply();

        PersistPrefMain persist = new PersistPrefMain(ApplicationProvider.getApplicationContext());
        ServiceLocator.getInstance().setPersistMain(persist);

        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launchInContainer(FragmentFrequencies.class);

        onView(withId(R.id.SectionFreqeuncyWallpaper)).perform(click());

        onData(anything())
                .inRoot(isDialog())
                .atPosition(0).perform(click());

        verify(taskMock).cancel();
    }

}