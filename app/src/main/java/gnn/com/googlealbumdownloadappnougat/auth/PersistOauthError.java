package gnn.com.googlealbumdownloadappnougat.auth;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Date;

import gnn.com.photos.sync.PersistTime;

public class PersistOauthError extends PersistTime {
    public PersistOauthError(@NonNull File processFolder) {
        super(processFolder, "oauth_error");
    }

    public boolean isInError() {
        Date date = retrieveTime();
        return date != null;
    }
}
