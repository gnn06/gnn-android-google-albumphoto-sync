package gnn.com.googlealbumdownloadappnougat.photos;

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

import java.io.File;
import java.io.IOException;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.photos.remote.PhotosRemoteService;

public class PhotosRemoteServiceAndroid extends PhotosRemoteService {

    private static final String TAG = "goi";
    private final Context activity;

    public PhotosRemoteServiceAndroid(MainActivity activity, File cacheFile) {
        super(cacheFile);
        this.activity = activity;
    }

    protected PhotosLibraryClient getPhotoLibraryClient()
            throws IOException, GoogleAuthException
    {
        if (client != null) {
            Log.d(TAG, "get photo library client from cache");
            return client;
        } else {
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
            PhotosLibraryClient client = PhotosLibraryClient.initialize(settings);
            this.client = client;
            return client;
        }
    }
}
