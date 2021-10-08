package gnn.com.googlealbumdownloadappnougat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.settings.SettingsActivity;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistenceMain;
import gnn.com.googlealbumdownloadappnougat.settings.PersistenceSettings;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterMain;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;

public class MainActivity extends AppCompatActivity implements IView {

    public static final int RC_SIGN_IN = 501;
    public static final int RC_AUTHORIZE_PHOTOS = 500;
    public static final int RC_AUTHORIZE_WRITE = 503;
    public static final int RC_CHOOSE_FOLDER = 504;

    private static final String TAG = "goi";

    private final UITextHelper UITextHelper = new UITextHelper(this);

    private PresenterMain presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new PresenterMain(this, this);

        new PersistenceMain(this).restore(presenter);
        new PersistenceSettings(this).restore(presenter);

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

        findViewById(R.id.SectionFolder).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.chooseFolder();
            }
        });

        findViewById(R.id.ChooseOneButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onButtonSyncOnce();
            }
        });

        findViewById(R.id.ButtonWallpaper).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onButtonWallpaperOnce();
            }
        });

        ((SwitchCompat)findViewById(R.id.SwitchWallPaper)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.onSwitchWallpaper(checked);
            }
        });

        presenter.init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        new PersistenceMain(this).save(presenter);
        // not necessary to save settings as settings can not be changed in this activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            presenter.onSignOut();
            return true;
        } else if (item.getItemId() == R.id.reset_cache) {
            presenter.onMenuResetCache();
            return true;
        } else if (item.getItemId() == R.id.scheduleDetails) {
            presenter.onMenuScheduleDetail();
            return true;
        } else if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult, requestCode="+ requestCode + ", resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN  || requestCode == RC_AUTHORIZE_PHOTOS) {
            presenter.handlePermission(resultCode == Activity.RESULT_OK ? Require.SUCCESS : Require.FAILURE);
        } else if (requestCode == RC_CHOOSE_FOLDER && resultCode == MainActivity.RESULT_OK) {
            // return to app without choosing a folder : data == null && resultCode == 0
            presenter.setFolder(data);
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
    public void onAlbumChosenResult(String albumName) {
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

    @Override
    public void setSwitchWallpaper(boolean scheduled) {
        SwitchCompat button = findViewById(R.id.SwitchWallPaper);
        button.setChecked(scheduled);
    }

    public void updateUI_User() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String name;
        if (account != null) {
            name = account.getEmail();
//            String GoogleAuthorization = account.getGrantedScopes().toString();
//            String writeAuthorization;
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//                writeAuthorization = "write";
//            else writeAuthorization = "no write";
//            name += "\n" + GoogleAuthorization + ", " + writeAuthorization;
        } else {
            name = getResources().getString(R.string.login);
        }

        TextView myAwesomeTextView = findViewById(R.id.textUser);
        myAwesomeTextView.setText(name);

        Log.d(TAG, "updateUI_User, account=" + (account == null ? "null" : account.getEmail()));
    }

    @Override
    public void updateUI_CallResult(SynchronizerAndroid synchronizer, SyncStep step) {
        String result = UITextHelper.getResultString(synchronizer, step, this);
        TextView textView = findViewById(R.id.result);
        textView.setText(result);
    }

    @Override
    public void updateUI_lastSyncTime(Date lastSyncTime, Date lastWallpaperTime) {
        if (lastSyncTime != null || lastWallpaperTime != null) {
            TextView textView = findViewById(R.id.result);
            DateFormat sdf = SimpleDateFormat.getInstance();
            String stringLastModified = lastSyncTime != null ? sdf.format(lastSyncTime) : null;
            String stringLastWallpaperTime = lastWallpaperTime != null ? sdf.format(lastWallpaperTime) : null;
            textView.setText(UITextHelper.getLastTimesString(stringLastModified, stringLastWallpaperTime));
        }
    }

    @Override
    public void updateUI_Folder(String humanPath) {
        TextView textView = findViewById(R.id.textFolder);
        textView.setText(humanPath);
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    @Override
    /*
      @return String "" if empty
     */
    public String getQuantity() {
        TextView view = findViewById(R.id.textQuantity);
        return view.getText().toString();
    }

    @Override
    /*
      @param quantity "" if no quantity
     */
    public void setQuantity(String quantity) {
        TextView view = findViewById(R.id.textQuantity);
        view.setText(quantity);
    }

    @Override
    public String getRename() {
        // getText returns "" if empty
        // transform "" into null
        String value = ((TextView) findViewById(R.id.textRename)).getText().toString();
        return "".equals(value) ? null : value;
    }

    @Override
    public void setRename(String rename) {
        // transform null into ""
        TextView view = findViewById(R.id.textRename);
        view.setText(rename == null ? "" : rename);
    }

    @Override
    public String getFrequencyWallpaper() {
        TextView view = findViewById(R.id.textFrequencyWallpaper);
        return view.getText().toString();
    }

    @Override
    public void setFrequencyWallpaper(String frequency) {
        TextView view = findViewById(R.id.textFrequencyWallpaper);
        view.setText(frequency);
    }

    @Override
    public void enableFrequencyWallpaper(boolean switchChecked) {
        View text = findViewById(R.id.textFrequencyWallpaper);
        text.setEnabled(!switchChecked);
    }

    @Override
    public String getFrequencySync() {
        TextView view = findViewById(R.id.textFrequencySync);
        return view.getText().toString();
    }

    @Override
    public void setFrequencySync(String frequency) {
        TextView view = findViewById(R.id.textFrequencySync);
        view.setText(frequency);
    }

    @Override
    public void enableFrequencySync(boolean switchChecked) {
        View text = findViewById(R.id.textFrequencySync);
        text.setEnabled(!switchChecked);
    }
}
