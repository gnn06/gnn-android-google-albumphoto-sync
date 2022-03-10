package gnn.com.googlealbumdownloadappnougat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.googlealbumdownloadappnougat.wallpaper.Notification;
import gnn.com.photos.stat.stat.WallpaperStat;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 501;
    public static final int RC_AUTHORIZE_PHOTOS = 500;
    public static final int RC_AUTHORIZE_WRITE = 503;
    public static final int RC_CHOOSE_FOLDER = 504;

    private static final String TAG = "goi";

    private final UITextHelper UITextHelper = new UITextHelper(this);

    private PresenterHome presenter;

    private PermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.configure(getCacheDir().getAbsolutePath());
        Logger.getLogger().fine("onCreate");

        permissionHandler = new PermissionHandler();

        final UserModel userModel = new ViewModelProvider(this).get(UserModel.class);
        userModel.getUser().observe(this, new Observer<GoogleSignInAccount>() {
            @Override
            public void onChanged(@Nullable GoogleSignInAccount account) {
                updateUI_User(account);
            }
        });

        presenter = new PresenterHome(this, this, permissionHandler, userModel);

        // avoid to call click event when initialize UI
        presenter.onAppStart();

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
                presenter.onChooseFolder();
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

        new Notification(this).createNotificationChannel();

        findViewById(R.id.warning_wallpaper_active).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onWarningWallpaperActive();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.getLogger().fine("onStart");
        presenter.onAppForeground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.getLogger().fine("onPause");
        PersistPrefMain preferences = new PersistPrefMain(this);
        preferences.save(presenter);
        preferences.saveDownloadOption(presenter);
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
        } else if (item.getItemId() == R.id.requestGooglePermission) {
            presenter.onMenuRequestGooglePermission();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult, requestCode="+ requestCode + ", resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN || requestCode == RC_AUTHORIZE_PHOTOS) {
            permissionHandler.handlePermission(resultCode == Activity.RESULT_OK ? Require.SUCCESS : Require.FAILURE);
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
                permissionHandler.handlePermission(Require.SUCCESS);
            } else {
                permissionHandler.handlePermission(Require.FAILURE);
            }
        }
    }


}
