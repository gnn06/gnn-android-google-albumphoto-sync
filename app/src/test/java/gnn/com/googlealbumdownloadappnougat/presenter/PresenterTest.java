package gnn.com.googlealbumdownloadappnougat.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.GooglePhotoAPI_Requirement;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionRequirement;
import gnn.com.googlealbumdownloadappnougat.auth.SignRquirement;
import gnn.com.googlealbumdownloadappnougat.auth.WritePermission;
import gnn.com.googlealbumdownloadappnougat.view.IView;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Test
    public void onSyncClick_NoAlbum() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", null);
        IView view = Mockito.spy(IView.class);
        presenter.onSyncClick();
        Mockito.verify(view, Mockito.never()).alertNoAlbum();
    }

    @Test
    public void onSyncClick_AllGrantedPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", "test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(true);
        Mockito.when(authMock.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.when(authMock.hasWritePermission()).thenReturn(true);
        presenter.setAuth(authMock);
        presenter.onSyncClick();
        assertNull(null, Whitebox.getInternalState(presenter, "permissionRequirement"));
    }

    @Test
    public void onSyncClick_MissingPermission() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        Whitebox.setInternalState(presenter, "album", "test");
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        Mockito.when(authMock.hasGooglePhotoPermission()).thenReturn(false);
        Mockito.when(authMock.hasWritePermission()).thenReturn(false);
        presenter.setAuth(authMock);
        presenter.onSyncClick();
        assertNotNull(Whitebox.getInternalState(presenter, "permissionRequirement"));
    }

    @Test
    public void onShowAlbumList_NoCache_NoPermission () {
        MainActivity activity = Mockito.mock(MainActivity.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity));
        AuthManager authMock = Mockito.mock(AuthManager.class);
        Mockito.when(authMock.isSignIn()).thenReturn(false);
        Mockito.when(authMock.hasGooglePhotoPermission()).thenReturn(false);
        Mockito.when(authMock.hasWritePermission()).thenReturn(false);
        presenter.setAuth(authMock);

        presenter.onShowAlbumList();

        assertNotNull(Whitebox.getInternalState(presenter, "permissionRequirement"));
    }

}