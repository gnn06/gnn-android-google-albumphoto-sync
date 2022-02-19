package gnn.com.photos.sync;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Date;

import gnn.com.photos.Photo;

public class PersistChooseTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void name() throws IOException {
        // when
        PersistChoose persist = new PersistChoose(temporaryFolder.newFolder());
        Photo photo = new Photo("url", "id");
        Date date = new Date(74, 3-1, 9, 12, 30);
        persist.write(photo);
        PhotoChoose photoResult = persist.read();
        assertThat(photoResult.photo.getId(), Is.is(photo.getId()));
    }
}