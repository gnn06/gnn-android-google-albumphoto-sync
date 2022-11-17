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

    @Before
    public void setUp() throws Exception {
        folder = temporaryFolder.newFolder();
    }

    @Test
    public void store() throws IOException {
        // given an Unit under test
        PersistSyncTime persistSyncTime = new PersistSyncTime(folder);
        // given
        Temp data = new Temp(12, 24, 48, new ArrayList<>(), new ArrayList(), 0, 0);

        // when
        persistSyncTime.storeTimeWithResult(data);

        // then
        String last_sync = FileUtils.readFileToString(new File(folder, PersistSyncTime.FILENAME), "UTF-8");
        assertThat(last_sync, is("{\"albumSize\":12,\"deleteCount\":24,\"downloadCount\":48}"));
    }

    @Test
    public void retrieve() throws IOException {
        // given an Unit under test
        PersistSyncTime persistSyncTime = new PersistSyncTime(folder);
        // given file
        FileUtils.write(new File(folder, PersistSyncTime.FILENAME), "{\"albumSize\":12,\"deleteCount\":24,\"downloadCount\":48}", "UTF-8");

        // when
        Temp result = persistSyncTime.retrieveTimeWithResult();
        Temp expected = new Temp(12, 24, 48, new ArrayList<>(), new ArrayList(), 0, 0);

        // then
        assertThat(result, is(equalTo(expected)));
    }
}