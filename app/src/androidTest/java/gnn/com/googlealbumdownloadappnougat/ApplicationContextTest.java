package gnn.com.googlealbumdownloadappnougat;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@Ignore
public class ApplicationContextTest {

    @Test
    public void getInstance() {
        ApplicationContext instance = ApplicationContext.getInstance(ApplicationProvider.getApplicationContext());
        System.out.println(instance.getCachePath());
        assertThat(instance.getCachePath(), equalTo("/data/user/0/gnn.com.googlealbumdownloadappnougat/files/cache"));
        assertThat(instance.getProcessPath(), equalTo("/data/user/0/gnn.com.googlealbumdownloadappnougat/files"));
    }
}