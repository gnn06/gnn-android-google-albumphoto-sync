package gnn.com.googlealbumdownloadappnougat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.googlealbumdownloadappnougat.view.IView;

public class MainActivity extends AppCompatActivity implements IView {

    public static final int RC_SIGN_IN = 501;
    public static final int RC_AUTHORIZE_PHOTOS = 500;
    public static final int RC_AUTHORIZE_WRITE = 503;

    private static final String TAG = "goi";

    public Scope SCOPE_PHOTOS_READ =
            new Scope("https://www.googleapis.com/auth/photoslibrary.readonly");

    private IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new Presenter(this, this);

        findViewById(R.id.btnChooseAlbum).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onAlbumChoose();
            }
        });

        findViewById(R.id.btnSync).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onSyncClick();
            }
        });

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart   ");
        super.onStart();
        updateUI_User();
        updateUI_Folder(presenter.getFolder());
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

    private PhotosLibraryClient mClient;

    public PhotosLibraryClient getPhotoLibraryClient() throws IOException, GoogleAuthException {
        if (mClient != null) {
            Log.d(TAG, "get photo library client from cache");
            return mClient;
        } else {
            /* Need an Id client OAuth in the google developer console of type android
             * Put the package and the fingerprint (gradle signingReport)
             */
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
            assert account != null && account.getAccount() != null;
            String token = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), "oauth2:profile email");
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
            this.mClient = client;
            return client;
        }
    }

    public void showChooseAlbumDialog(final ArrayList<String> mAlbums) {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mAlbums);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose album")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setAdapter(itemsAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String albumName = mAlbums.get(which);
                        presenter.onAlbumChoosen(albumName);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAlbumChoosenResult(String albumName) {
        TextView textView = findViewById(R.id.textAlbum);
        textView.setText(albumName);
    }

    private void updateUI_Folder(File folder) {
        TextView textView = findViewById(R.id.textFolder);
        textView.setText(folder.getPath());
    }


    private CharSequence getFolderName() {
        TextView textView = findViewById(R.id.textFolder);
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
            presenter.laucnhSync();
        }
    }

    private void handleAuthorizePhotos() {
        Log.d(TAG, "handleAuthorizePhotos");
        updateUI_User();
        presenter.laucnhSync();
    }

    private void handleAuthorizeWrite() {
        Log.d(TAG, "handle write permission");
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        assert account != null;
        presenter.launchSynchWithPermission();
    }

    @Override
    public void onGetAlbumsProgressBar(int visible) {
        ProgressBar pb = findViewById(R.id.pbChooseFolder);
        pb.setVisibility(visible);
    }

    public void updateUI_User() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        TextView myAwesomeTextView = findViewById(R.id.textUser);
        String name = account != null ? account.getEmail() : "";
        myAwesomeTextView.setText(name);
        TextView autorisationText = findViewById(R.id.textAutorisation);
        String autorisation = account != null ? account.getGrantedScopes().toString() : "";
        String writeAutorisation = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? "write" : "no write";
        CharSequence autorisationLabel = autorisation + ", " + writeAutorisation;
        autorisationText.setText(autorisationLabel);
        Log.d(TAG, "updateUI_User, account=" + (account == null ? "null" : account.getEmail()));
    }

    @Override
    public void updateUI_CallResult(String result) {
        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }

    private Context getActivity() {
        return this;
    }

    public void setSyncProgresBarVisibility(int visible) {
        ProgressBar pb = findViewById(R.id.pbSync);
        pb.setVisibility(visible);
    }
}
