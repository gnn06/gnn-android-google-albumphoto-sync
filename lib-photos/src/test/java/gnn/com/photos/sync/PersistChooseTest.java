package gnn.com.photos.sync;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

import gnn.com.photos.Photo;

public class PersistChooseTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void read() throws IOException {
        // given
        File folder = temporaryFolder.newFolder();
        PersistChoose persist = new PersistChoose(folder);
        final File tempFile = new File(folder, PersistChoose.FILENAME);
        Files.write(tempFile.toPath(), "{\"photo\":{\"url\":\"url1\",\"id\":\"id12\"},\"chooseDate\":\"Feb 1, 2022, 9:44:28 PM\"}".getBytes(StandardCharsets.UTF_8));
        // when
        PhotoChoose photoResult = persist.read();
        // then
        assertThat(photoResult.photo.getId(), Is.is("id12"));
    }

    @Test
    public void read_no_file() throws IOException {
        // given
        PersistChoose persist = new PersistChoose(temporaryFolder.newFolder());
        // when
        PhotoChoose photoResult = persist.read();
        // then
        assertThat(photoResult, nullValue());
    }

    @Test
    public void read_malformed_file() throws IOException {
        // given
        File folder = temporaryFolder.newFolder();
        final File tempFile = new File(folder, PersistChoose.FILENAME);
        Files.write(tempFile.toPath(), "{\"photoXXX\":{\"url\":\"url1\",\"id\":\"id12\"},\"chooseDateXXX\":\"Feb 1, 2022, 9:44:28 PM\"}".getBytes(StandardCharsets.UTF_8));

        PersistChoose persist = new PersistChoose(folder);
        // when
        PhotoChoose photoResult = persist.read();
        // then
        assertThat(photoResult, nullValue());
    }
}