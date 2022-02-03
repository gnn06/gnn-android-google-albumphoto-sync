package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

public class PersistWallpaperStatTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
//    @Ignore
    public void write() throws IOException {
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, "wallpaper.json");
        tempFile.delete();
        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat stat = new WallpaperStat(12, new Date());
        persistent.write(stat);
        assertThat(tempFile.exists(), Is.is(true));
    }

    @Test
//    @Ignore
    public void read() throws IOException {
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, "wallpaper.json");
        Files.write(tempFile.toPath(), "{\"nbChangeOnLastDay\":24,\"lastChangeDate\":\"Feb 1, 2022, 9:44:28 PM\"}".getBytes(StandardCharsets.UTF_8));

        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat result = persistent.read();
        assertThat(result.getNbChangeOnLastDay(), is(24));
    }
}