package gnn.com.photos.sync;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;
import gnn.com.photos.service.SyncProgressObserver;

public class SyncMethodRenameTest {

    ArrayList<Photo> remotePhotos;
    ArrayList<Photo> localPhotos;
    PhotosRemoteService prs;
    PhotosLocalService pls;
    Synchronizer synchronizer;
    File folder;
    private SyncProgressObserver observer;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException, RemoteException {
        remotePhotos = new ArrayList<>();
        localPhotos = new ArrayList<>();

        remotePhotos.add(new Photo("url1", "id1"));
        remotePhotos.add(new Photo("url2", "id2"));

        localPhotos.add(new Photo("name1", "name1"));
        localPhotos.add(new Photo("name2", "name2"));
        localPhotos.add(new Photo("name3", "name3"));

        prs = mock(PhotosRemoteService.class);
        pls = mock(PhotosLocalService.class);


        synchronizer = spy(new Synchronizer(prs, pls, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return prs;
            }

            @Override
            public void incAlbumSize() {}
        });

        folder = mock(File.class);

        doAnswer(invocation -> {
            ((SyncProgressObserver) invocation.getArgument(1)).incAlbumSize();
            return remotePhotos;
        }).when(prs).getPhotos(eq("album"), any());
        Mockito.when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);
        doAnswer(invocation -> {
            ((SyncProgressObserver) invocation.getArgument(3)).incCurrentDownload();
            return null;
        }).when(prs).download(any(), any(), anyString(), any());
        doAnswer(invocation -> {
            ((SyncProgressObserver) invocation.getArgument(2)).incCurrentDelete();
            return null;
        }).when(pls).delete(any(), any(), any());
    }

    @Test
    public void delete_after_rename_noObserver() throws IOException, RemoteException {
        // given

        // when
        synchronizer.sync("album", folder, "name", -1);

        // then
        ArrayList<Photo> expectedToDelete = new ArrayList<>(Arrays.asList(
                new Photo("name3", "name3")
        ));
        Mockito.verify(pls).delete(expectedToDelete, folder, synchronizer);
        Mockito.verify(synchronizer).incCurrentDownload();
        Mockito.verify(synchronizer).incCurrentDelete();
        Mockito.verify(synchronizer).incAlbumSize();
    }

    @Test
    public void delete_after_rename_withObserver() throws IOException, RemoteException {
        // given
        SyncProgressObserver observer = mock(SyncProgressObserver.class);
        synchronizer.setObserver(observer);

        // when
        synchronizer.sync("album", folder, "name", -1);

        // then
        ArrayList<Photo> expectedToDelete = new ArrayList<>(Arrays.asList(
                new Photo("name3", "name3")
        ));
        Mockito.verify(pls).delete(expectedToDelete, folder, synchronizer);
        Mockito.verify(synchronizer).incCurrentDownload();
        Mockito.verify(synchronizer).incCurrentDelete();
        Mockito.verify(synchronizer).incAlbumSize();

        Mockito.verify(observer).incCurrentDownload();
        Mockito.verify(observer).incCurrentDelete();
        //Ignore
        //Mockito.verify(observer).incAlbumSize();
    }

}