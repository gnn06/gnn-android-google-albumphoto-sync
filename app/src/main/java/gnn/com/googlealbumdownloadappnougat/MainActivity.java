// TODO change package
package gnn.com.googlealbumdownloadappnougat;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;

import java.io.File;
import java.io.IOException;

import gnn.com.photos.sync.Synchronizer;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 501;
    private static final int RC_CHOOSE_FOLDER = 502;
    private static final int RC_AUTHORIZE_PHOTOS = 500;
    private static final int RC_AUTHORIZE_WRITE = 503;

    private static final String TAG = "goi";

    Scope SCOPE_PHOTOS_READ =
            new Scope("https://www.googleapis.com/auth/photoslibrary.readonly");

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.btnSync).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onGetAlbumsClick();
            }
        });

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart   ");
        super.onStart();
        updateUI_User();
        updateUI_Album("test");
        updateUI_Folder(folder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult, requestCode="+ requestCode + ", resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN  && resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (RC_AUTHORIZE_PHOTOS == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                handleAuthorizePhotos();
            } else {
                updateUI_CallResult("Cancel");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (RC_AUTHORIZE_WRITE == requestCode) {
            updateUI_User();
            handleAuthorizeWrite();
        }
    }

    private void onGetAlbumsClick() {
        // TODO try to simplify callback cascade :-/
        Log.d(TAG, "onGetAlbumsClick");
        if (GoogleSignIn.getLastSignedInAccount(getActivity()) == null) {
            Log.d(TAG,"onGetAlbumsClick, user does not be connected => SignInIntent");
            updateUI_User();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            Log.d(TAG, "start signin intent");
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            if (!GoogleSignIn.hasPermissions(
                    GoogleSignIn.getLastSignedInAccount(getActivity()),
                    SCOPE_PHOTOS_READ)) {
                Log.d(TAG,"onGetAlbumsClick, user already signin, do not have permissions => requestPermissions");
                // doc said that if account is null, should ask but instead cancel the request or create a bug.
                GoogleSignIn.requestPermissions(
                        MainActivity.this,
                        RC_AUTHORIZE_PHOTOS,
                        GoogleSignIn.getLastSignedInAccount(getActivity()),
                        SCOPE_PHOTOS_READ);
            } else {
                Log.d(TAG,"onGetAlbumsClick, user already signin, already have permissions => laucnhSync()");
                laucnhSync();
            }
        }

    }

    private void updateUI_Album(String albumName) {
        TextView textView = findViewById(R.id.textAlbum);
        textView.setText(albumName);
    }

    private void updateUI_Folder(File folder) {
        TextView textView = findViewById(R.id.textFolder);
        textView.setText(folder.getPath());
    }

    private File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    private CharSequence getFolderName() {
        TextView textView = findViewById(R.id.textFolder);
        return textView.getText();
    }

    private File getFolder() {
        return folder;
    }

    private CharSequence getAlbum() {
        TextView textView = findViewById(R.id.textAlbum);
        return textView.getText();
    }

    private void handleSignInResult (Task<GoogleSignInAccount> completedTask) {
        Log.d(TAG, "handleSignInResult");
        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        updateUI_User();
        if (!GoogleSignIn.hasPermissions(
                account,
                SCOPE_PHOTOS_READ)) {
            Log.d(TAG, "signin done, do not have permissions => request permissions");
            GoogleSignIn.requestPermissions(
                    MainActivity.this,
                    RC_AUTHORIZE_PHOTOS,
                    account,
                    SCOPE_PHOTOS_READ);
        } else {
            Log.d(TAG, "signin done, have permissions => laucnhSync()");
            laucnhSync();
        }
    }

    private void handleAuthorizePhotos() {
        Log.d(TAG, "handleAuthorizePhotos");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI_User();
        laucnhSync();
    }

    private void laucnhSync() {
        Log.d(TAG, "laucnhSync");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (account != null) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "request WRITE permission");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_AUTHORIZE_WRITE);
            } else {
                Log.d(TAG,"WRITE permission granted, call google");
                SyncTask task = new SyncTask(account.getAccount());
                task.execute();
            }
        }
    }

    private class SyncTask extends AsyncTask<Void, Void, String> {

        Account mAccount;

        public SyncTask(Account account) {
            mAccount = account;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                /* Need an Id client OAuth in the google developer console of type android
                 * Put the package and the fingerprint (gradle signingReport)
                 */
                String token = GoogleAuthUtil.getToken(getApplicationContext(), mAccount, "oauth2:profile email");
                OAuth2Credentials userCredentials = OAuth2Credentials.newBuilder()
                        .setAccessToken(new AccessToken(token, null))
                        .build();
                PhotosLibrarySettings settings =
                        PhotosLibrarySettings.newBuilder()
                                .setCredentialsProvider(
                                        FixedCredentialsProvider.create(
                                                userCredentials))
                                .build();
                PhotosLibraryClient client = PhotosLibraryClient.initialize(settings);

                String album = getAlbum().toString();
                File destination = getFolder();

                Synchronizer sync = new Synchronizer();
                sync.sync(album, destination, client);

                Log.d(TAG,"end");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return "done";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateUI_CallResult("in progress");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateUI_CallResult(s);
        }

    }
    private void handleAuthorizeWrite() {
        Log.d(TAG, "handle write permission");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        SyncTask task = new SyncTask(account.getAccount());
        task.execute();
    }
    private void updateUI_User() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        TextView myAwesomeTextView = findViewById(R.id.textUser);
        String name = account != null ? account.getEmail() : "";
        myAwesomeTextView.setText(name);
        TextView autorisationText = findViewById(R.id.textAutorisation);
        String autorisation = account != null ? account.getGrantedScopes().toString() : "";
        String writeAutorisation = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? "write" : "no write";
        autorisationText.setText(autorisation + ", " + writeAutorisation);
        Log.d(TAG, "updateUI_User, account=" + (account == null ? "null" : account.getEmail()));
    }
    private void updateUI_CallResult(String result) {
        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }

    private Context getActivity() {
        return this;
    }
}
