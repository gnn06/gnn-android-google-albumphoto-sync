package gnn.com.googlealbumdownloadappnougat;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int RC_AUTHORIZE_PHOTOS = 500;
    private static final int RC_SIGN_IN = 501;
    private static final String TAG = "goi";

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("goi", "goi");
                signIn();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("goi", "goi");

                Scope SCOPE_PHOTOS_READ =
                        new Scope("https://www.googleapis.com/auth/photoslibrary.readonly");

                if (!GoogleSignIn.hasPermissions(
                        GoogleSignIn.getLastSignedInAccount(getActivity()),
                        SCOPE_PHOTOS_READ)) {
                    GoogleSignIn.requestPermissions(
                            MainActivity.this,
                            RC_AUTHORIZE_PHOTOS,
                            GoogleSignIn.getLastSignedInAccount(getActivity()),
                            SCOPE_PHOTOS_READ);
                } else {
                    getAlbumNames();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI_User(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.i("goi", "sign in result");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (RC_AUTHORIZE_PHOTOS == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                getAlbumNames();
            } else {
                updateUI_CallResult("Cancel");
            }
        }
    }

    private void signIn() {
        updateUI_User(null);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI_User(account);
            Log.i(TAG, "user sign-in to google api" + account.getEmail());

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI_User(null);
        }
    }

    private void updateUI_User(GoogleSignInAccount account) {
        TextView myAwesomeTextView = (TextView)findViewById(R.id.myAwesomeTextView);
        String name = account != null ? account.getEmail() : "";
        myAwesomeTextView.setText(name);
        Log.d(TAG, "updateUI_User");
    }

    private void updateUI_CallResult(String result) {
        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }

    private void getAlbumNames() {
        Log.d("goi", "getAlbumNames");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (account != null) {
            Log.d("goi","call google");
            GetAlbumsTask task = new GetAlbumsTask(account.getAccount());
            task.execute();
        }
    }

    private class GetAlbumsTask extends AsyncTask<Void, Void, String> {

        Account mAccount;
        public GetAlbumsTask(Account account) {
            mAccount = account;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String concatAlbumName = "";
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
                InternalPhotosLibraryClient.ListAlbumsPagedResponse response = client.listAlbums();

                for (Album album : response.iterateAll()) {
                       concatAlbumName += album.getTitle();
                }

                Log.d("goi","end");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return concatAlbumName;
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

    private Context getActivity() {
        return this;
    }
}
