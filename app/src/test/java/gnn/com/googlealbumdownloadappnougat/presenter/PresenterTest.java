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
    public void onChooseAlbum() {
    }


    @Test
    public void onSyncClick() {
        MainActivity activity = Mockito.mock(MainActivity.class);
        AuthManager auth = Mockito.mock(AuthManager.class);
        Presenter presenter = Mockito.spy(new Presenter(activity, activity, auth));
        Mockito.when(auth.isSignIn()).thenReturn(true);
        presenter.onSyncClick();
        Mockito.verify(auth).signIn();

    }
}