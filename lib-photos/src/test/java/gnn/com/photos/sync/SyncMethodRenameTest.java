package gnn.com.photos.sync;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import gnn.com.photos.Photo;
import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.service.RemoteException;

public class SyncMethodRenameTest {

    ArrayList<Photo> remotePhotos;
    ArrayList<Photo> localPhotos;
    PhotosRemoteService prs;
    PhotosLocalService pls;
    Synchronizer synchronizer;
    File folder;

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

        prs = Mockito.mock(PhotosRemoteService.class);
        pls = Mockito.mock(PhotosLocalService.class);


        synchronizer = new Synchronizer(null, 0, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return prs;
            }

            @Override
            public void incAlbumSize() {}
        };

        folder = Mockito.mock(File.class);

        Mockito.when(prs.getPhotos("album", synchronizer)).thenReturn(remotePhotos);
        Mockito.when(pls.getLocalPhotos(folder)).thenReturn(localPhotos);

    }

    @Test
    public void delete_after_rename() throws IOException, RemoteException {
        // given

        // when
        SyncMethod syncMethod = new SyncMethod(synchronizer, prs, pls);
        syncMethod.sync("album", folder, "name", -1);

        // then
        ArrayList<Photo> expectedToDelete = new ArrayList<>(Arrays.asList(
                new Photo("name3", "name3")
        ));
        Mockito.verify(pls).delete(expectedToDelete, folder, synchronizer);
    }

}