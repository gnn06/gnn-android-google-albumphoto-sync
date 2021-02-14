package gnn.com.photos.sync;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


import gnn.com.photos.model.Photo;

public class DownloadManagerTest {

    private ArrayList<Photo> toDownloadList;
    private Synchronizer synchronizer;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        toDownloadList = new ArrayList<>();
        Photo photo = new Photo("http://gn.com/", "id12");
        toDownloadList.add(photo);

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
        verify(synchronizer, times(1)).incCurrentDownload();
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
        verify(downloader).copy((URL)anyObject(), argument.capture());
        assertEquals("id12.jpg", argument.getValue().getName());
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
        verify(downloader).copy((URL)anyObject(), argument.capture());
        assertEquals("name1.jpg", argument.getValue().getName());
    }
}