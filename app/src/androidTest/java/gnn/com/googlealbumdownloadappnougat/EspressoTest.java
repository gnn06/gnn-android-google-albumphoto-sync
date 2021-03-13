package gnn.com.googlealbumdownloadappnougat;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

//    @Test
//    public void AlbumClick() {
//        onView(withId(R.id.SectionAlbum)).perform(click());
//    }

    @Test
    public void showError() throws Throwable {
        activityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityRule.getActivity().showError("message");
            }
        });
    }
}
