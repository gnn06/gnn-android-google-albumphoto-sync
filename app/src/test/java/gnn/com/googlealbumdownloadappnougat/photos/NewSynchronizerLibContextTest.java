package gnn.com.googlealbumdownloadappnougat.photos;

import static org.hamcrest.CoreMatchers.equalTo;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import gnn.com.photos.LibContext;
import gnn.com.photos.service.IScreenSizeAccessor;

@RunWith(MockitoJUnitRunner.class)
public class NewSynchronizerLibContextTest {

    @Mock IScreenSizeAccessor screenSize;

    @Mock Context context;

    @Test
    public void newSynchronizerAndroid() {
        SynchronizerAndroid synchronizer = new SynchronizerAndroid(context, null, 0, null, screenSize);
        IScreenSizeAccessor actual = LibContext.getScreenSizeAccessor();
        Assert.assertThat(actual, equalTo(screenSize));
    }

    @Test
    public void newSynchronizerAndroidTask() {
        SynchronizerAndroid synchronizer = new SynchronizerTask(context, null, 0, null, screenSize);
        IScreenSizeAccessor actual = LibContext.getScreenSizeAccessor();
        Assert.assertThat(actual, equalTo(screenSize));
    }

}