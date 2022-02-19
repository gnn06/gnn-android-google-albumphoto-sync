package gnn.com.photos.service;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import gnn.com.photos.Photo;
import gnn.com.photos.sync.Synchronizer;

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

        synchronizer = Mockito.mock(Synchronizer.class);
    }

    @Test
    public void download_incCurrentDownload() throws IOException {
        // check incCurrentDownload is called

        // which mock copy
        DownloadManager downloader = Mockito.spy(new DownloadManager());
        Mockito.doNothing().when(downloader).copy((URL) ArgumentMatchers.anyObject(), (File) ArgumentMatchers.anyObject());

        // when
        downloader.download(toDownloadList,
                temporaryFolder.getRoot(),
                null, synchronizer);
        // then
        Mockito.verify(synchronizer, Mockito.times(2)).incCurrentDownload();
    }

    @Test
    public void download_no_renamming() throws IOException {
        // which mock copy
        DownloadManager downloader = Mockito.spy(new DownloadManager());

        // when
        downloader.download(toDownloadList,
                temporaryFolder.getRoot(),
                null, synchronizer);

        // then
        ArgumentCaptor<File> argument = ArgumentCaptor.forClass(File.class);
        Mockito.verify(downloader, Mockito.times(2)).copy((URL) ArgumentMatchers.anyObject(), argument.capture());

        List<File> allArguments = argument.getAllValues();
        Assert.assertEquals("id12.jpg", allArguments.get(0).getName());
        Assert.assertEquals("id24.jpg", allArguments.get(1).getName());
    }

    @Test
    public void download_and_rename() throws IOException {
        // given a downloader
        DownloadManager downloader = Mockito.spy(new DownloadManager());
        // which mock copy

        // when calling download
        downloader.download(toDownloadList, temporaryFolder.getRoot(), "name", synchronizer);

        // then
        ArgumentCaptor<File> argument = ArgumentCaptor.forClass(File.class);
        Mockito.verify(downloader, Mockito.times(2)).copy((URL) ArgumentMatchers.anyObject(), argument.capture());

        List<File> allArguments = argument.getAllValues();

        Assert.assertEquals("name1.jpg", allArguments.get(0).getName());
        Assert.assertEquals("name2.jpg", allArguments.get(1).getName());
    }

    @Ignore
    @Test
    public void copy_overwrite() throws IOException {
        final File tmpFile = new File(System.getProperty("java.io.tmpdir"), "toto");
        FileUtils.writeStringToFile(tmpFile, "content", "iso-8859-1");
        FileUtils.copyURLToFile(new URL("http://www.google.fr"), new File(System.getProperty("java.io.tmpdir"), "toto"));
        String read = FileUtils.readFileToString(tmpFile, "iso-8859-1");
        Assert.assertTrue(read.contains("<!doctype"));
    }
}