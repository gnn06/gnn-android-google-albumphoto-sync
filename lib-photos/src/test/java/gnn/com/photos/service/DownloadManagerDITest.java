package gnn.com.photos.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.LibContext;
import gnn.com.photos.Photo;
import gnn.com.util.ScreenSize;

public class DownloadManagerDITest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private DownloadManager downloader;

    private ArrayList<Photo> toDownloadList;
    private SyncProgressObserver observer;
    private IScreenSizeAccessor screenSizeAccessor;
    private PhotoSizeUrl photoSizer;
    private ScreenSize screenSize;

    @Before
    public void setUp() throws Exception {
        Logger.configure();
        toDownloadList = new ArrayList<>();
        toDownloadList.add(new Photo("http://gn.com/12", "id12"));
        observer = mock(SyncProgressObserver.class);
        screenSizeAccessor = mock(IScreenSizeAccessor.class);
        screenSize = new ScreenSize(1024, 2048);
        when(screenSizeAccessor.get()).thenReturn(screenSize);
        photoSizer = mock(PhotoSizeUrl.class);
        downloader = spy(new DownloadManager(photoSizer, screenSize));
        LibContext.initialize(null);
    }

    @Test
    public void constructor() throws IOException {
        // Check that DownloadManger's constructor inject ScreenSize from LibContext.

        // Given
        LibContext.initialize(screenSizeAccessor);

        // when
        DownloadManager downloadManager = new DownloadManager();

        // Then
        assertThat(downloadManager.screenSize, is(screenSize));
    }

    @Test
    public void main() throws IOException {
        // Check that ScreenSizeAccesor is used when photoSizer is called

        // Given
        LibContext.initialize(screenSizeAccessor);
        doNothing().when(downloader).copy(any(), any());

        // when
        downloader.download(toDownloadList,
                temporaryFolder.getRoot(),
                null, observer);

        // Then
        verify(photoSizer).getUrl(any(), eq(screenSize));
    }

    @Test(expected = NullPointerException.class)
    public void no_initialize() throws IOException {
        // Check that exception throws if libcontext is not initialize

        // Given
        // No initialize

        // when
        new DownloadManager();

        // Then
        // Expected NullPointer exception
    }
}