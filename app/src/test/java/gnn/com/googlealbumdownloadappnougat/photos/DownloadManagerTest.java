package gnn.com.googlealbumdownloadappnougat.photos;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.photos.model.Photo;
import gnn.com.photos.sync.DownloadManager;

public class DownloadManagerTest {

    @Test
    public void download() {
        // check incCurrentDownload is called
        MainActivity activity = Mockito.mock(MainActivity.class);

        ArrayList<Photo> toDownloadList = new ArrayList<>();
        Photo photo = new Photo("http://gn.com/", "id12");
        toDownloadList.add(photo);
        File folder = new File("c:/temp/");

        SynchronizerAndroid synchronizerSpy = Mockito.spy(new SynchronizerAndroid(activity, null, null));
        synchronizerSpy.setSyncTask(Mockito.mock(SyncTask.class));
        try {
            DownloadManager.download(toDownloadList,
                    folder,
                    synchronizerSpy);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.verify(synchronizerSpy, Mockito.times(1)).incCurrentDownload();
    }
}