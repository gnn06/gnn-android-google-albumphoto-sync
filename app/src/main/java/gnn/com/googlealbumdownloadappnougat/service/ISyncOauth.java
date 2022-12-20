package gnn.com.googlealbumdownloadappnougat.service;

import android.util.Log;

import com.google.api.gax.rpc.PermissionDeniedException;

import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.photos.service.RemoteException;

public interface ISyncOauth<T> {

    PersistOauthError getPersistOAuth();

    default T execOauth() throws RemoteException {
        try {
            syncOauthStart();
            return execOauthImpl();
        } catch (RemoteException| PermissionDeniedException e) {
            syncOauthException();
            throw new RemoteException(e);
        }
    }

    T returnFailure();

    T execOauthImpl() throws RemoteException;

    default void syncOauthStart() {
        getPersistOAuth().reset();
    }

    default void syncOauthException() {
        try {
            getPersistOAuth().storeTime();
        } catch (IOException e) {
            Log.e("GNNAPP", e.toString());
        }

    }
}
