package gnn.com.photos.stat.stat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.io.IOException;

import gnn.com.util.DateProvider;

public class WallpaperStatProviderTest {

    @Test
    public void no_previous_stat() throws IOException {
        PersistWallpaperStat persistMock = mock(PersistWallpaperStat.class);
        when(persistMock.read()).thenReturn(null);
        WallpaperStatProvider updater = new WallpaperStatProvider(persistMock, new DateProvider());
        updater.onWallpaperChange();
        verify(persistMock, atLeastOnce()).write(any());


    }
}