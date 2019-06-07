package gnn.com.googlealbumdownloadappnougat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.googlealbumdownloadappnougat.view.IView;

public class MainActivity extends AppCompatActivity implements IView {

    public static final int RC_SIGN_IN = 501;
    public static final int RC_AUTHORIZE_PHOTOS = 500;
    public static final int RC_AUTHORIZE_WRITE = 503;

    private static final String TAG = "goi";

    public IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new Presenter(this, this, new AuthManager(this));

        findViewById(R.id.btnChooseAlbum).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onShowAlbumList();
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
            presenter.handleSignInResult();
        } else if (RC_AUTHORIZE_PHOTOS == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "handleAuthorizePhotos");
                updateUI_User();
                presenter.laucnhSync();
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
            Log.d(TAG, "handle write permission");
            presenter.launchSynchWithPermission();
        }
    }

    public void showChooseAlbumDialog(final ArrayList<String> mAlbums) {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAlbums);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose album")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setAdapter(itemsAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String albumName = mAlbums.get(which);
                        presenter.onChooseAlbum(albumName);
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

    @Override
    public void alertNoAlbum() {
        new AlertDialog.Builder(this)
                .setTitle("You have to choose an album")
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    private void updateUI_Folder(File folder) {
        TextView textView = findViewById(R.id.textFolder);
        textView.setText(folder.getPath());
    }

    private CharSequence getFolderName() {
        TextView textView = findViewById(R.id.textFolder);
        return textView.getText();
    }

    @Override
    public void setChooseAlbumProgressBarVisibility(int visible) {
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

    @Override
    public void setSyncProgresBarVisibility(int visible) {
        ProgressBar pb = findViewById(R.id.pbSync);
        pb.setVisibility(visible);
    }
}
