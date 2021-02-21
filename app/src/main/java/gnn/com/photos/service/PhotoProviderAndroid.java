package gnn.com.photos.service;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;

import java.io.IOException;

public class PhotoProviderAndroid extends PhotoProvider {

    private static final String TAG = "PhotoProviderAndroid";

    private final Context activity;

    public PhotoProviderAndroid(Context activity) {
        super();
        this.activity = activity;
    }

    public PhotosLibraryClient getClient()
            throws IOException, GoogleAuthException
    {
        if (client != null) {
            Log.d(TAG, "get photo library client from cache");
            return client;
        } else {
            return getClientImpl();
        }
    }

    PhotosLibraryClient getClientImpl()
            throws IOException, GoogleAuthException {
        /* Need an Id client OAuth in the google developer console of type android
         * Put the package and the fingerprint (gradle signingReport)
         */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        assert account != null && account.getAccount() != null;
        String token = GoogleAuthUtil.getToken(activity.getApplicationContext(), account.getAccount(), "oauth2:profile email");
        OAuth2Credentials userCredentials = OAuth2Credentials.newBuilder()
                .setAccessToken(new AccessToken(token, null))
                .build();
        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(
                                FixedCredentialsProvider.create(
                                        userCredentials))
                        .build();
        return PhotosLibraryClient.initialize(settings);
    }
}
