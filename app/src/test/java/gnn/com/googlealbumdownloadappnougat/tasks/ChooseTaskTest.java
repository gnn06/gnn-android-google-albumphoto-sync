package gnn.com.googlealbumdownloadappnougat.tasks;

import com.google.android.gms.auth.GoogleAuthException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.Presenter;
import gnn.com.photos.sync.Synchronizer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChooseTaskTest {

    @Test
    public void syncAll() throws IOException, GoogleAuthException {
        SynchronizerAndroid synchroniser = mock(SynchronizerAndroid.class);
        IPresenter presenter = mock(Presenter.class);
        ChooseTask chooseTask = new ChooseTask(presenter, synchroniser);
        // given
        when(presenter.getQuantity()).thenReturn(-1);
        // when
        chooseTask.doInBackground(null);
        // then
        verify(synchroniser).syncAll(anyString(), (File) anyObject());
    }

    @Test
    public void syncRandom() throws IOException, GoogleAuthException {
        SynchronizerAndroid synchroniser = mock(SynchronizerAndroid.class);
        IPresenter presenter = mock(Presenter.class);
        ChooseTask chooseTask = new ChooseTask(presenter, synchroniser);
        // given
        when(presenter.getQuantity()).thenReturn(5);
        // when
        chooseTask.doInBackground(null);
        // then
        verify(synchroniser).syncRandom(anyString(), (File) anyObject(), anyInt());
    }
}