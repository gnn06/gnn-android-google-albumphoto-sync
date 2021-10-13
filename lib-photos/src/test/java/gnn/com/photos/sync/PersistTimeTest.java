package gnn.com.photos.sync;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class PersistTimeTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    public void no_exist_file() throws IOException {
        File folder = tmpFolder.newFolder();
        PersistTime persistent = new PersistSyncTime(folder);
        // when
        persistent.reset();
        // then
        File file = new File(folder, "last_sync");
        assertFalse(file.exists());
    }

    @Test
    public void existing_file() throws IOException {
        // given
        File folder = tmpFolder.newFolder();
        PersistTime persistent = new PersistSyncTime(folder);
        File file = new File(folder, "last_sync");
        file.createNewFile();
        assertTrue(file.exists());
        // when
        persistent.reset();
        // then
        assertFalse(file.exists());
    }

    @Test
    public void with_null() throws IOException {
        File folder = tmpFolder.newFolder();
        PersistTime persistent = new PersistSyncTime(null);
        // when
        persistent.reset();
        // then
        File file = new File(folder, "last_sync");
        assertFalse(file.exists());
    }
}