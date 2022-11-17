package gnn.com.googlealbumdownloadappnougat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.testing.TestLifecycleOwner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LiveDataTest {

    @Test
    public void name() {
        Observer<String> observer = mock(Observer.class);
        MutableLiveData<String> data = new MutableLiveData<>();
        LifecycleOwner owner = new TestLifecycleOwner(Lifecycle.State.STARTED);
        data.observe(owner, observer);
        data.setValue("change");
        verify(observer).onChanged("change");
        // STARTED, RESUMED OK
        // CREATED, INITIALIZED KO
        // DESTROYED error
    }
}
