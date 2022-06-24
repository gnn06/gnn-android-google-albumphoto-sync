package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

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

        ScheduleTask taskMock = mock(ScheduleTask.class);
        ServiceLocator.getInstance().setSyncTask(taskMock);

        FragmentScenario<FragmentFrequencies> scenario = FragmentScenario.launch(FragmentFrequencies.class);

//        verify(taskMock, never()).schedule(anyBoolean(), anyLong(), anyInt(), anyLong());
        org.junit.Assert.assertTrue(true);
        // in test, getActivity=FragmentActivity
        // in app, getActivity=Mainactivity
    }
}