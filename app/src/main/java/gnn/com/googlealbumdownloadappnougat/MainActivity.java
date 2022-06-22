package gnn.com.googlealbumdownloadappnougat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import gnn.com.googlealbumdownloadappnougat.auth.AuthManager;
import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.auth.Require;
import gnn.com.googlealbumdownloadappnougat.ui.FolderModel;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterMain;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.googlealbumdownloadappnougat.wallpaper.Notification;
import gnn.com.googlealbumdownloadappnougat.wallpaper.WallpaperScheduler;
import gnn.com.googlealbumdownloadappnougat.wizard.PresenterWizard;
import gnn.com.googlealbumdownloadappnougat.wizard.ViewModelWizard;

public class MainActivity extends AppCompatActivity implements IView {

    public static final int RC_SIGN_IN = 501;
    public static final int RC_AUTHORIZE_PHOTOS = 500;
    public static final int RC_AUTHORIZE_WRITE = 503;

    private static final String TAG = "goi";

    private final UITextHelper UITextHelper = new UITextHelper(this);

    private PresenterMain presenter;
    private PresenterWizard presenterWizard;

    private PermissionHandler permissionHandler;

    private AuthManager auth;
    private UserModel userModel;
    private FolderModel folderModel;
    private ViewModelWizard wizardModel;
    private PresenterHome presenterHome;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.configure(getCacheDir().getAbsolutePath());
        Logger.getLogger().fine("onCreate");

        ServiceLocator.getInstance().setWallpaperScheduler(new WallpaperScheduler(getApplicationContext()));

        permissionHandler = new PermissionHandler();
        auth = new AuthManager(this);

        userModel = new ViewModelProvider(this).get(UserModel.class);
        folderModel = new ViewModelProvider(this).get(FolderModel.class);
        wizardModel = new ViewModelProvider(this).get(ViewModelWizard.class);

        presenter = new PresenterMain(auth, this, userModel, permissionHandler, this, presenterHome);
        presenterWizard = new PresenterWizard(this, null, wizardModel);
        presenterWizard.onAppStart();

        new Notification(this).createNotificationChannel();

        final NavHostFragment fragmentById = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        final NavController navController = fragmentById.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container_view);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Used by fragment to access shared permission handler ; avoid singleton
     */
    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuLogout) {
            presenter.onSignOut();
            return true;
        } else if (item.getItemId() == R.id.menuReset_cache) {
            presenter.onMenuResetCache();
            return true;
        } else if (item.getItemId() == R.id.menuScheduleDetails) {
            presenter.onMenuScheduleDetail();
            return true;
        } else if (item.getItemId() == R.id.menuRequestGooglePermission) {
            presenter.onMenuRequestGooglePermission();
            return true;
        } else if (item.getItemId() == R.id.menuStartWizard) {
            new PresenterWizard(this, null, this.wizardModel).onShowWizard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, requestCode="+ requestCode + ", resultCode=" + resultCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN || requestCode == RC_AUTHORIZE_PHOTOS) {
            permissionHandler.handlePermission(resultCode == Activity.RESULT_OK ? Require.SUCCESS : Require.FAILURE);
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


    @Override
    public void showError(String message) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle(getResources().getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    public void makeVisible(boolean visible) {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(400);
        transition.addTarget(R.id.fragment_container_wizard);
        transition.addTarget(R.id.fragment_container_view);
        ViewGroup parent = findViewById(R.id.mainview);
        TransitionManager.beginDelayedTransition(parent, transition);
        if (visible) {
            findViewById(R.id.fragment_container_wizard).setVisibility(View.VISIBLE);
        } else if (!visible) {
            findViewById(R.id.fragment_container_wizard).setVisibility(View.GONE);
        }
    }
}
