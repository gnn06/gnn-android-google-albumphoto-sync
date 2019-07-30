package gnn.com.photos.sync;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.photos.model.Photo;

public class DownloadManagerTest {

    @Test
    public void download() {
        // check incCurrentDownload is called

        ArrayList<Photo> toDownloadList = new ArrayList<>();
        Photo photo = new Photo("http://gn.com/", "id12");
        toDownloadList.add(photo);
        File folder = new File("c:/temp/");
        Synchronizer synchronizerSpy = Mockito.spy(new Synchronizer(Mockito.mock(Presenter.SyncTask.class)));
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