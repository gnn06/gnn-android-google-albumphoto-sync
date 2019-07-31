package gnn.com.googlealbumdownloadappnougat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.presenter.IPresenter;
import gnn.com.googlealbumdownloadappnougat.presenter.Persistence;
import gnn.com.googlealbumdownloadappnougat.presenter.Presenter;
import gnn.com.googlealbumdownloadappnougat.view.IView;
import gnn.com.photos.sync.Synchronizer;

public class MainActivity extends AppCompatActivity implements IView {

    public static final int RC_SIGN_IN = 501;
    public static final int RC_AUTHORIZE_PHOTOS = 500;
    public static final int RC_AUTHORIZE_WRITE = 503;
    public static final int RC_CHOOSE_FOLDER = 504;

    private static final String TAG = "goi";

    private IPresenter presenter;
    private Persistence persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new Presenter(this, this);

        this.persistence = new Persistence(this, presenter);
        persistence.restoreData();

        updateUI_User();

        findViewById(R.id.SectionUser).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onSignIn();
            }
        });

        findViewById(R.id.SectionAlbum).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onShowAlbumList();
            }
        });

        findViewById(R.id.SectionSync).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onSyncClick();
            }
        });

        findViewById(R.id.SectionFolder).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.chooseFolder();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                presenter.onSignOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistence.saveData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult, requestCode="+ requestCode + ", resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN  || requestCode == RC_AUTHORIZE_PHOTOS) {
            presenter.handlePermission(resultCode == Activity.RESULT_OK ? Require.SUCCESS : Require.FAILURE);
        } else if (requestCode == RC_CHOOSE_FOLDER && resultCode == MainActivity.RESULT_OK) {
            // TODO: 30/07/2019 manage resultCode != OK
            presenter.chooseFolderResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (RC_AUTHORIZE_WRITE == requestCode) {
            Log.d(TAG, "handle write permission");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handlePermission(Require.SUCCESS);
            } else {
                presenter.handlePermission(Require.FAILURE);
            }
        }
    }

    public void showChooseAlbumDialog(final ArrayList<String> mAlbums) {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAlbums);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_album))
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
                .setTitle(getResources().getString(R.string.need_album))
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void setProgressBarVisibility(int visible) {
        View pb = findViewById(R.id.pb_layout);
        pb.setVisibility(visible);
    }

    public void updateUI_User() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String name;
        if (account != null) {
            name = account.getEmail();
            String GoogleAuthorization = account.getGrantedScopes().toString();
            String writeAuthorization;
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                writeAuthorization = "write";
            else writeAuthorization = "no write";
            name += "\n" + GoogleAuthorization + ", " + writeAuthorization;
        } else {
            name = getResources().getString(R.string.login);
        }

        TextView myAwesomeTextView = findViewById(R.id.textUser);
        myAwesomeTextView.setText(name);

        Log.d(TAG, "updateUI_User, account=" + (account == null ? "null" : account.getEmail()));
    }

    @Override
    public void updateUI_CallResult(Synchronizer synchronizer, SyncStep step) {
        String result = "";
        switch (step) {
            case STARTING:
                result += getResources().getString(R.string.sync_starting);
                break;
            case IN_PRORGESS:
                result += getResources().getString(R.string.sync_in_progress) + "\n";
                result += getResultText(synchronizer, false);
                break;
            case FINISHED:
                result += getResources().getString(R.string.sync_finished) + "\n";
                result += getResultText(synchronizer, true);
                break;
        }

        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }

    String getResultText(Synchronizer synchronizer, boolean finished) {
        String result = "";
        result += "downloaded = ";
        if (!finished) {
            result += synchronizer.getCurrentDownload() + " / ";
        }
        result += synchronizer.getTotalDownload();
        result += "\n";
        result += "deleted = ";
        if (!finished) {
            result += synchronizer.getCurrentDelete() + " / ";
        }
        result += synchronizer.getTotalDelete();
        return result;
    }

    @Override
    public void updateUI_Folder(String humanPath) {
        TextView textView = findViewById(R.id.textFolder);
        textView.setText(humanPath);
    }

}
