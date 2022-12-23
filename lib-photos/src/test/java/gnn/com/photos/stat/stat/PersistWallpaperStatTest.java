package gnn.com.photos.stat.stat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class PersistWallpaperStatTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        Logger.configure();
        Logger.getLogger();
    }

    @Test
    public void write() throws IOException {
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, PersistWallpaperStat.STAT_FILENAME);
        tempFile.delete();
        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat stat = new WallpaperStat(12, 14, new Date());
        persistent.write(stat);
        assertThat(tempFile.exists(), Is.is(true));
    }

    @Test
    public void read() throws IOException {
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, PersistWallpaperStat.STAT_FILENAME);
        Files.write(tempFile.toPath(), "{\"changeOnDate\":24,\"changeOnDayBefore\":36,\"date\":\"Feb 1, 2022, 9:44:28 PM\"}".getBytes(StandardCharsets.UTF_8));

        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat result = persistent.read();
        assertThat(result.getChangeOnDate(), is(24));
        assertThat(result.getChangeOnDayBefore(), is(36));
        assertThat(result.getDate(), is(new Date(122, 1, 1, 21, 44, 28)));
    }

    @Test
    public void read_missing_file() throws IOException {
        File folder = tempFolder.newFolder();

        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat result = persistent.read();
        assertThat(result, nullValue());
    }

    @Test(expected = NullPointerException.class)
    public void read_empty_file() throws IOException {
        // Given
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, PersistWallpaperStat.STAT_FILENAME);
        Files.write(tempFile.toPath(), "".getBytes(StandardCharsets.UTF_8));
        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        // when
        WallpaperStat result = persistent.read();
        // then
    }

    @Test
    public void read_too_much_fields() throws IOException {
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, PersistWallpaperStat.STAT_FILENAME);
        Files.write(tempFile.toPath(), "{\"changeOnDate\":24,\"changeOnDayBefore\":36,\"date\":\"Feb 1, 2022, 9:44:28 PM\", \"toto\":12}".getBytes(StandardCharsets.UTF_8));

        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat result = persistent.read();
        assertThat(result.getChangeOnDate(), is(24));
    }

    @Test
    public void read_missing_fields() throws IOException {
        File folder = tempFolder.newFolder();
        final File tempFile = new File(folder, PersistWallpaperStat.STAT_FILENAME);
        Files.write(tempFile.toPath(), "{\"changeOnDate\":24}".getBytes(StandardCharsets.UTF_8));

        PersistWallpaperStat persistent = new PersistWallpaperStat(folder);
        WallpaperStat result = persistent.read();
        assertThat(result, nullValue());
    }
}