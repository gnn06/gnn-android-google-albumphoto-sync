package gnn.com.photos.sync;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class ChooseOneLocalPhotoPersistTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void name() throws IOException {
        ChooseOneLocalPhotoPersist instance = ChooseOneLocalPhotoPersist.getInstance();
        assertThat(instance.isConfigured(), is(false));
        File file1 = temporaryFolder.newFolder();
        File file2 = temporaryFolder.newFolder();
        ChooseOneLocalPhotoPersist.getInstance(file1, file2);
        assertThat(instance.isConfigured(), is(true));
    }
}