package gnn.com.googlealbumdownloadappnougat.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Test
    public void onSyncClick_not_signin() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(false);
        presenter.onSyncClick();
        Mockito.verify(auth).signIn();
    }

    @Test
    public void onSyncClick_alreadySignIn_NotGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(false);
        presenter.onSyncClick();
        Mockito.verify(auth, Mockito.never()).signIn();
        Mockito.verify(auth).requestGooglePhotoPermission();
        Mockito.verify(presenter, Mockito.never()).laucnhSync();
    }

    @Test
    public void onSyncClick_alreadySignIn_alreadeyGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(true);
        Mockito.when(auth.hasGooglePhotoPermission()).thenReturn(true);
        Mockito.doNothing().when(presenter).laucnhSync();
        presenter.onSyncClick();
        Mockito.verify(auth, Mockito.never()).signIn();
        Mockito.verify(presenter).laucnhSync();
    }

    @Test
    public void laucnhSync_alreadyGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.hasWritePermission()).thenReturn(true);
        Mockito.doNothing().when(presenter).launchSynchWithPermission();
        presenter.laucnhSync();
        Mockito.verify(presenter).launchSynchWithPermission();
    }

    @Test
    public void laucnhSync_NotGranted() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.hasWritePermission()).thenReturn(false);
        presenter.laucnhSync();
        Mockito.verify(auth).requestWritePermission();
    }

    @Test
    public void launchSynchWithPermission_NoAlbum() {
        MainActivity activity = Mockito.spy(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);

        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Whitebox.setInternalState(presenter, "album", "");

        Mockito.doNothing().when(activity).alertNoAlbum();

        presenter.launchSynchWithPermission();

        Mockito.verify(activity).alertNoAlbum();
    }

}