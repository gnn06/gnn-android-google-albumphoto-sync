package gnn.com.googlealbumdownloadappnougat.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import gnn.com.googlealbumdownloadappnougat.MainActivity;

public class AuthManager {

    public Scope SCOPE_PHOTOS_READ =
            new Scope("https://www.googleapis.com/auth/photoslibrary.readonly");


    public AuthManager(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;

    public boolean isSignIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        return account != null && account.getAccount() != null;
    }

    public  boolean hasGooglePermission() {
        // normal granted scopes
        // https://www.googleapis.com/auth/photoslibrary.readonly
        // https://www.googleapis.com/auth/userinfo.profile
        // https://www.googleapis.com/auth/userinfo.email
        // openid, profile, emaol
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        return GoogleSignIn.hasPermissions(account, SCOPE_PHOTOS_READ)&& GoogleSignIn.hasPermissions(account, new Scope(Scopes.EMAIL));
    }

    public void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.EMAIL))
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN);
    }

    public void signInWithPermission() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.EMAIL), SCOPE_PHOTOS_READ)
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, MainActivity.RC_AUTHORIZE_PHOTOS);
    }

    public boolean isAuthorized() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        return GoogleSignIn.hasPermissions(account, SCOPE_PHOTOS_READ, new Scope(Scopes.EMAIL));
    }

    public void requestGooglePermission() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        GoogleSignIn.requestPermissions(activity, MainActivity.RC_AUTHORIZE_PHOTOS, account, new Scope(Scopes.EMAIL), SCOPE_PHOTOS_READ);
    }

    public void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);
        googleSignInClient.revokeAccess();
        googleSignInClient.signOut();
    }

    public boolean hasWritePermission() {
        return activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestWritePermission() {
        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.RC_AUTHORIZE_WRITE);
    }

    public GoogleSignInAccount getAccount() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        return account;
    }
}
