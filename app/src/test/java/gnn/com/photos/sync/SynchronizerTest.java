package gnn.com.photos.sync;

import org.junit.Test;

import gnn.com.photos.service.PhotosRemoteService;

import static org.mockito.Mockito.*;

public class SynchronizerTest {

    @Test
    public void resetCache() {
        Synchronizer sync = new Synchronizer(null, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return mock(PhotosRemoteService.class);
            }

            @Override
            public void incAlbumSize() {

            }
        };
        sync.resetCache();
    }
}