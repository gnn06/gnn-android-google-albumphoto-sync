package gnn.com.googlealbumdownloadappnougat.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;

public class AuthManager {

    public Scope SCOPE_PHOTOS_READ =
            new Scope("https://www.googleapis.com/auth/photoslibrary.readonly");


    public AuthManager(MainActivity activity) {
        this.activity = activity;
    }

    private MainActivity activity;

    public boolean isSignIn() {
        return GoogleSignIn.getLastSignedInAccount(activity) != null;
    }

    public void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN);
    }

    public void requestGooglePhotoPermission() {
        GoogleSignIn.requestPermissions(
                activity,
                MainActivity.RC_AUTHORIZE_PHOTOS,
                GoogleSignIn.getLastSignedInAccount(activity),
                SCOPE_PHOTOS_READ);
    }

    public boolean hasGooglePhotoPermission() {
        return GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(activity),
                SCOPE_PHOTOS_READ);
    }

    public boolean hasWritePermission() {
        return activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestWritePermission() {
        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.RC_AUTHORIZE_WRITE);
    }
}
