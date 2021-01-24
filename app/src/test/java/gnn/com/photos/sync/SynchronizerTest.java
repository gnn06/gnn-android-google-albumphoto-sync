package gnn.com.photos.sync;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import gnn.com.photos.service.PhotosLocalService;
import gnn.com.photos.model.Photo;
import gnn.com.photos.service.PhotosRemoteService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class SynchronizerTest {

    @Test
    public void resetCache() {
        Synchronizer sync = new Synchronizer(null) {
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