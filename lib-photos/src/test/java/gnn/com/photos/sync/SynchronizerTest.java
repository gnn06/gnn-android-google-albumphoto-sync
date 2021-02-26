package gnn.com.photos.sync;

import org.junit.Test;
import org.mockito.Mockito;

import gnn.com.photos.service.PhotosRemoteService;

import static org.mockito.Mockito.*;

public class SynchronizerTest {

    @Test
    public void resetCache() {
        Synchronizer sync = new Synchronizer(null, 24 * 60 * 60 * 1000, null) {
            @Override
            protected PhotosRemoteService getRemoteServiceImpl() {
                return Mockito.mock(PhotosRemoteService.class);
            }

            @Override
            public void incAlbumSize() {

            }
        };
        sync.resetCache();
    }
}