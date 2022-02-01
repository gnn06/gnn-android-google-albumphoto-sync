package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

public class WallpaperStatPersistentTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
//    @Ignore
    public void write() throws IOException {
        final File tempFile = new File("wallpaper.json");
        tempFile.delete();
        WallpaperStatPersistent persistent = new WallpaperStatPersistent();
        WallpaperStat stat = new WallpaperStat(12, new Date());
        persistent.write(stat);
        assertThat(tempFile.exists(), Is.is(true));
    }

    @Test
//    @Ignore
    public void read() throws IOException {
        final File tempFile = new File("wallpaper.json");
        Files.write(tempFile.toPath(), "{\"changeByDay\":24,\"lastWallpaper\":\"Feb 1, 2022, 9:44:28 PM\"}".getBytes(StandardCharsets.UTF_8));

        WallpaperStatPersistent persistent = new WallpaperStatPersistent();
        WallpaperStat result = persistent.read();
        assertThat(result.getChangeByDay(), is(24));
    }
}