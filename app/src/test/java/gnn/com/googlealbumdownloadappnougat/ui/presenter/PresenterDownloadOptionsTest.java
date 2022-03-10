package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.FragmentDownloadOptions;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewDoanloadOptions;

public class PresenterDownloadOptionsTest {

    @Test
    public void getQuantity_empty() {
        IViewDoanloadOptions view = mock(FragmentDownloadOptions.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterDownloadOptions presenter = new PresenterDownloadOptions(view);

        when(view.getQuantity()).thenReturn("");

        int actual = presenter.getQuantity();

        assertEquals(-1, actual);
    }

    @Test
    public void getQuantity_notEmpty() {
        IViewDoanloadOptions view = mock(FragmentDownloadOptions.class);
        MainActivity activity = mock(MainActivity.class);
        IPresenterDownloadOptions presenter = new PresenterDownloadOptions(view);

        when(view.getQuantity()).thenReturn("5");

        int actual = presenter.getQuantity();

        assertEquals(5, actual);
    }

    @Test
    public void setQuantity_notEmpty() {
        IViewDoanloadOptions view = mock(FragmentDownloadOptions.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterDownloadOptions presenter = new PresenterDownloadOptions(view);

        presenter.setQuantity(5);

        verify(view).setQuantity("5");
    }

    @Test
    public void setQuantity_empty() {
        IViewDoanloadOptions view = mock(FragmentDownloadOptions.class);
        MainActivity activity = mock(MainActivity.class);
        PresenterDownloadOptions presenter = new PresenterDownloadOptions(view);

        presenter.setQuantity(-1);

        verify(view).setQuantity("");
    }
}