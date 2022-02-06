package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

@RunWith(MockitoJUnitRunner.class)
public class StatUpdaterWithPersistTest {
    @Test
    public void no_previous_stat() throws IOException {
        PersistWallpaperStat persistMock = mock(PersistWallpaperStat.class);
        when(persistMock.read()).thenReturn(null);
        StatUpdaterWithPersist updater = new StatUpdaterWithPersist(persistMock, new DateProvider());
        updater.onWallpaperChange();
        verify(persistMock, atLeastOnce()).write(any());


    }
}