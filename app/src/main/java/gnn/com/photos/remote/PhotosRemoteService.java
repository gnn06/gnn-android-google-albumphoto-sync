package gnn.com.photos.remote;

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
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.photos.model.Photo;

public class PhotosRemoteService {

    private static final String TAG = "goi";
    private PhotosLibraryClient client;
    private final Context activity;

    public PhotosRemoteService(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * Retrieve remote album list
     */
    public ArrayList<String> getAlbums() throws IOException, GoogleAuthException {
        ArrayList<String> albumNames = new ArrayList<>();
        InternalPhotosLibraryClient.ListAlbumsPagedResponse albums = getPhotoLibraryClient().listAlbums();
        for (Album album : albums.iterateAll()) {
            albumNames.add(album.getTitle());
        }
        return albumNames;
    }

    /**
     * Retrieve remote photo list.
     */
    public ArrayList<Photo> getRemotePhotos(String albumName) throws IOException, GoogleAuthException {

        InternalPhotosLibraryClient.ListAlbumsPagedResponse response = getPhotoLibraryClient().listAlbums();
        for (Album album : response.iterateAll()) {
            String title = album.getTitle();
            if (albumName.equals(title)) {
                String albumId = album.getId();
                //System.out.println("albumId=" + albumId);
                InternalPhotosLibraryClient.SearchMediaItemsPagedResponse responsePhotos = getPhotoLibraryClient().searchMediaItems(albumId);
                ArrayList<Photo> result = new ArrayList<>();
                for (MediaItem item : responsePhotos.iterateAll()) {
                    result.add(new Photo(item.getBaseUrl(), item.getId()));
                }
                return result;
            }
        }
        return null;
    }

    private PhotosLibraryClient getPhotoLibraryClient()
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
