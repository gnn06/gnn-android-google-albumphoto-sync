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
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int RC_AUTHORIZE_CONTACTS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("goi", "goi");

                Scope SCOPE_CONTACTS_READ =
                        new Scope("https://www.googleapis.com/auth/contacts.readonly");
                Scope SCOPE_EMAIL = new Scope(Scopes.EMAIL);

                if (!GoogleSignIn.hasPermissions(
                        GoogleSignIn.getLastSignedInAccount(getActivity()),
                        SCOPE_CONTACTS_READ,
                        SCOPE_EMAIL)) {
                    GoogleSignIn.requestPermissions(
                            MainActivity.this,
                            RC_AUTHORIZE_CONTACTS,
                            GoogleSignIn.getLastSignedInAccount(getActivity()),
                            SCOPE_CONTACTS_READ,
                            SCOPE_EMAIL);
                } else {
                    getContacts();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (RC_AUTHORIZE_CONTACTS == requestCode) {
                getContacts();
            }
        }
    }

    private void updateUI_Result(String result) {
        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }

    private void getContacts() {
        Log.d("goi", "getContacts");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (account != null) {
            Log.d("goi","call google");
            GetContactsTask task = new GetContactsTask(account.getAccount());
            task.execute();
        }
    }

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private class GetContactsTask extends AsyncTask<Void, Void, String> {

        Account mAccount;
        public GetContactsTask(Account account) {
            mAccount = account;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String concatAlbumName = "";
            try {
                String token = GoogleAuthUtil.getToken(getApplicationContext(), mAccount, "oauth2:profile email");
                GoogleCredential credential = new GoogleCredential().setAccessToken(token);
                String clientId = "745624147612-7r3jfsn3o44a0gbd822d745avhmb77hk.apps.googleusercontent.com";
                String clientSecret = "PHKbBrJvOr9CUswinBG3P91m";
                UserCredentials userCredentials = UserCredentials.newBuilder()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRefreshToken(credential.getRefreshToken())
                        .setAccessToken(new AccessToken(credential.getAccessToken(), null))
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
            updateUI_Result("in progress");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateUI_Result(s);
        }
    }

    private Context getActivity() {
        return this;
    }
}
