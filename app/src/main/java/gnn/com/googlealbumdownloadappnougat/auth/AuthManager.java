package gnn.com.googlealbumdownloadappnougat.auth;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;

public class AuthManager {

    public AuthManager(MainActivity activity) {
        this.activity = activity;
    }

    private MainActivity activity;

    public boolean isSignIn() {
        return GoogleSignIn.getLastSignedInAccount(activity) == null;
    }

    public void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN);
    }

    public void requestGooglePhotoPermission(Presenter presenter) {
        GoogleSignIn.requestPermissions(
                activity,
                MainActivity.RC_AUTHORIZE_PHOTOS,
                GoogleSignIn.getLastSignedInAccount(activity),
                presenter.SCOPE_PHOTOS_READ);
    }

    public boolean hasGooglePhotoPermission(Presenter presenter) {
        return GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(activity),
                presenter.SCOPE_PHOTOS_READ);
    }
}
