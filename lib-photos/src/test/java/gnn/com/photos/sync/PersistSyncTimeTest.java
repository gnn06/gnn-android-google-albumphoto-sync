package gnn.com.photos.sync;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PersistSyncTimeTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File folder;
    private PersistSyncTime persistSyncTime;

    @Before
    public void setUp() throws Exception {
        folder = temporaryFolder.newFolder();
        persistSyncTime = new PersistSyncTime(folder);
    }

    @Test
    public void store() throws IOException {
        // given
        SyncData data = new SyncData(12, 24, 48, new ArrayList<>(), new ArrayList(), 0, 0);

        // when
        persistSyncTime.storeTimeWithResult(data);

        // then
        String last_sync = FileUtils.readFileToString(new File(folder, PersistSyncTime.FILENAME), "UTF-8");
        assertThat(last_sync, is("{\"albumSize\":12,\"deleteCount\":24,\"downloadCount\":48}"));
    }

    @Test
    public void retrieve() throws IOException {
        // given file
        FileUtils.write(new File(folder, PersistSyncTime.FILENAME), "{\"albumSize\":12,\"deleteCount\":24,\"downloadCount\":48}", "UTF-8");

        // when
        SyncData result = persistSyncTime.retrieveSyncResult();
        SyncData expected = new SyncData(12, 24, 48, null, null, 0, 0);

        // then
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void retrieve_old_format() throws IOException {
        // given file
        FileUtils.write(new File(folder, PersistSyncTime.FILENAME), "", "UTF-8");

        // when
        SyncData result = persistSyncTime.retrieveSyncResult();
        SyncData expected = null;

        // then
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void retrieve_nofile() throws IOException {
        // given file
        // no file

        // when
        SyncData result = persistSyncTime.retrieveSyncResult();
        SyncData expected = null;

        // then
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void retrieve_missing_property() throws IOException {
        // given file
        FileUtils.write(new File(folder, PersistSyncTime.FILENAME), "{\"albumSize\":12,\"deleteCount\":24}", "UTF-8");

        // when
        SyncData result = persistSyncTime.retrieveSyncResult();
        SyncData expected = new SyncData(12, 24, 0, null, null, 0, 0);

        // then
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void retrieve_too_much_property() throws IOException {
        // given file
        FileUtils.write(new File(folder, PersistSyncTime.FILENAME), "{\"albumSize\":12,\"deleteCount\":24,\"downloadCount\":48,\"foo\":96}", "UTF-8");
        // when
        SyncData result = persistSyncTime.retrieveSyncResult();
        SyncData expected = new SyncData(12, 24, 48, null, null, 0, 0);

        // then
        assertThat(result, is(equalTo(expected)));
    }
}