package gnn.com.googlealbumdownloadappnougat.auth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.util.Logger;

public class PersistOauthErrorTest {

    // unit under test
    private PersistOauthError persist;

    private File folder;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        Logger.configure();
        folder = temporaryFolder.newFolder();
        persist = new PersistOauthError(folder);
    }

    @Test
    public void store() throws IOException {
        // when
        persist.storeTime();
    }

    @Test
    public void isnError_false() {
        // when
        boolean inError = persist.isInError();
        assertThat(inError, is(false));
    }

    @Test
    public void isnError_true() throws IOException {
        // given
        persist.storeTime();
        // when
        boolean inError = persist.isInError();
        // then
        assertThat(inError, is(true));
    }
}