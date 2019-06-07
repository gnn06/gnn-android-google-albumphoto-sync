package gnn.com.googlealbumdownloadappnougat.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;

import static org.junit.Assert.*;

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
}