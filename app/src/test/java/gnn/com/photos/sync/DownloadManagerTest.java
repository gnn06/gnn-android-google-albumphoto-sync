package gnn.com.photos.sync;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import gnn.com.photos.model.Photo;

public class DownloadManagerTest {

    private ArrayList<Photo> toDownloadList;
    private Synchronizer synchronizer;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("http://gn.com/12", "id12"));
        toDownloadList.add(new Photo("http://gn.com/24", "id24"));

        synchronizer = mock(Synchronizer.class);
    }

    @Test
    public void download_incCurrentDownload() throws IOException {
        // check incCurrentDownload is called

        // which mock copy
        DownloadManager downloader = spy(new DownloadManager());
        doNothing().when(downloader).copy((URL)anyObject(), (File)anyObject());

        // when
        downloader.download(toDownloadList,
                temporaryFolder.getRoot(),
                null, synchronizer);
        // then
        verify(synchronizer, times(2)).incCurrentDownload();
    }

    @Test
    public void download_no_renamming() throws IOException {
        // which mock copy
        DownloadManager downloader = spy(new DownloadManager());

        // when
        downloader.download(toDownloadList,
                temporaryFolder.getRoot(),
                null, synchronizer);

        // then
        ArgumentCaptor<File> argument = ArgumentCaptor.forClass(File.class);
        verify(downloader, times(2)).copy((URL)anyObject(), argument.capture());

        List<File> allArguments = argument.getAllValues();
        assertEquals("id12.jpg", allArguments.get(0).getName());
        assertEquals("id24.jpg", allArguments.get(1).getName());
    }

    @Test
    public void download_and_rename() throws IOException {
        // given a downloader
        DownloadManager downloader = spy(new DownloadManager());
        // which mock copy

        // when calling download
        downloader.download(toDownloadList, temporaryFolder.getRoot(), "name", synchronizer);

        // then
        ArgumentCaptor<File> argument = ArgumentCaptor.forClass(File.class);
        verify(downloader, times(2)).copy((URL)anyObject(), argument.capture());

        List<File> allArguments = argument.getAllValues();

        assertEquals("name1.jpg", allArguments.get(0).getName());
        assertEquals("name2.jpg", allArguments.get(1).getName());
    }

    @Test
    public void copy_overwrite() throws IOException {
        final File tmpFile = new File(System.getProperty("java.io.tmpdir"), "toto");
        FileUtils.writeStringToFile(tmpFile, "content", "iso-8859-1");
        FileUtils.copyURLToFile(new URL("http://www.google.fr"), new File(System.getProperty("java.io.tmpdir"), "toto"));
        String read = FileUtils.readFileToString(tmpFile, "iso-8859-1");
        assertTrue(read.contains("<!doctype"));
    }
}