package gnn.com.googlealbumdownloadappnougat.photos;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.DownloadManager;

public class DownloadManagerTest {

    private ArrayList<Photo> toDownloadList;
    private SynchronizerAndroid synchronizer;

    @Before
    public void setUp() throws Exception {
        toDownloadList = new ArrayList<>();
        Photo photo = new Photo("http://gn.com/", "id12");
        toDownloadList.add(photo);

        synchronizer = mock(SynchronizerAndroid.class);
    }

    @Test
    public void download_call_synchronizerIncCurrentDownload() {
        // check incCurrentDownload is called
        MainActivity activity = mock(MainActivity.class);

        // when
        try {
            DownloadManager.download(toDownloadList,
                    null,
                    synchronizer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(synchronizer, times(1)).incCurrentDownload();
    }

    @Test
    public void download_and_rename() throws IOException {
        // when calling download
        DownloadManager.download(toDownloadList, null, synchronizer);
    }
}