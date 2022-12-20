package gnn.com.googlealbumdownloadappnougat.service;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.gax.rpc.PermissionDeniedException;
import com.google.api.gax.rpc.StatusCode;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.photos.service.RemoteException;

public class ISyncOauthTest {

    // Unit under test
    ISyncOauth<Integer> syncOauth = spy(new ISyncOauth<Integer>() {
        @Override
        public PersistOauthError getPersistOAuth() {
            return mock(PersistOauthError.class);
        }

        @Override
        public Integer execOauthImpl() throws RemoteException {
            return 0;
        }

        @Override
        public Integer returnFailure() {
            return -1;
        }
    });

    @Test
    public void reset_before_exec() throws RemoteException {
        // when
        syncOauth.execOauth();
        // then
        verify(syncOauth).syncOauthStart();
        verify(syncOauth).execOauthImpl();
    }

    @Test (expected = RemoteException.class)
    public void catch_on_impl_exception() throws RemoteException {
        when(syncOauth.execOauthImpl()).thenThrow(new PermissionDeniedException("gnn", new Exception("inner"), mock(StatusCode.class), false));
        // when
        syncOauth.execOauth();
        // then
        verify(syncOauth).syncOauthException();
    }
}