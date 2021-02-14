package gnn.com.photos.sync;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.DownloadManager;

public class DownloadManagerTest {

    private ArrayList<Photo> toDownloadList;
    private Synchronizer synchronizer;

    @Before
    public void setUp() throws Exception {
        toDownloadList = new ArrayList<>();
        Photo photo = new Photo("http://gn.com/", "id12");
        toDownloadList.add(photo);

        synchronizer = mock(Synchronizer.class);
    }

    @Test
    public void download_call_synchronizerIncCurrentDownload() throws IOException {
        // check incCurrentDownload is called

        // which mock copy
        DownloadManager downloader = spy(new DownloadManager());
        doNothing().when(downloader).copy((URL)anyObject(), (File)anyObject());

        // when
        downloader.download(toDownloadList,
                    null,
                    synchronizer);
        verify(synchronizer, times(1)).incCurrentDownload();
    }

    @Test
    public void download_and_rename() throws IOException {
        // given a downloader
        DownloadManager downloader = spy(new DownloadManager());
        // which mock copy
        doNothing().when(downloader).copy((URL)anyObject(), (File)anyObject());

        // when calling download
        downloader.download(toDownloadList, null, synchronizer);

        // then
    }
}