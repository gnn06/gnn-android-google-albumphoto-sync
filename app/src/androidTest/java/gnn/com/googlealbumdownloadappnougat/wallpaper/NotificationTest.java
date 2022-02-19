package gnn.com.googlealbumdownloadappnougat.wallpaper;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class NotificationTest {

    @Test
    public void name() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Notification notification = new Notification(context);
        notification.createNotificationChannel();
        notification.show("message");
    }
}