package gnn.com.googlealbumdownloadappnougat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.auth.PermissionHandler;
import gnn.com.googlealbumdownloadappnougat.photos.SynchronizerAndroid;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterHome;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;
import gnn.com.photos.stat.stat.WallpaperStat;

public class FragmentHome extends Fragment implements IView {

    private static final String TAG = "FRAGMENTHOME";
    private IPresenterHome presenter;
    private PermissionHandler permissionHandler;

    public FragmentHome() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        permissionHandler = new PermissionHandler();
        presenter = new PresenterHome(this, (MainActivity) getActivity(), permissionHandler);
        view.findViewById(R.id.sectionDownloadOptions).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(getActivity(), R.id.fragment_container_view);
                controller.navigate(R.id.action_FragmentHome_to_FragmentDownloadOptions);
            }
        });
        view.findViewById(R.id.sectionFrequencies).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(getActivity(), R.id.fragment_container_view);
                controller.navigate(R.id.action_fragmentHome_to_fragmentFrequencies);
            }
        });
    }

    @Override
    public void onAlbumChosenResult(String albumName) {
        TextView textView = getView().findViewById(R.id.textAlbum);
        textView.setText(albumName);
    }

    public void showChooseAlbumDialog(final ArrayList<String> mAlbums) {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAlbums);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    public void setProgressBarVisibility(int visible) {
        View pb = getView().findViewById(R.id.pb_layout);
        pb.setVisibility(visible);
    }

    @Override
    public void alertNoAlbum() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.need_album))
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    public void updateUI_User() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        String name;
        if (account != null) {
            name = account.getEmail();
            String GoogleAuthorization = account.getGrantedScopes().toString();
            name += "\nGoogle permission=" + GoogleAuthorization;
//            String writeAuthorization;
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//                writeAuthorization = "write";
//            else writeAuthorization = "no write";
//            name += "\n" + GoogleAuthorization + ", " + writeAuthorization;
            name += "\nIsExpired() = " + account.isExpired();
        } else {
            name = getResources().getString(R.string.login);
        }

        TextView myAwesomeTextView = getView().findViewById(R.id.textUser);
        myAwesomeTextView.setText(name);

        Log.d(TAG, "updateUI_User, account=" + (account == null ? "null" : account.getEmail()));
    }

    @Override
    public void updateUI_CallResult(SynchronizerAndroid synchronizer, SyncStep step) {
        String result = new UITextHelper((MainActivity) getActivity()).getResultString(synchronizer, step, (MainActivity) getActivity());
        TextView textView = getView().findViewById(R.id.result);
        textView.setText(result);
    }

    @Override
    public void updateUI_lastSyncTime(Date lastUpdatePhotosTime, Date lastSyncTime, Date lastWallpaperTime) {
        DateFormat sdf = SimpleDateFormat.getInstance();
        // TODO 09/10 visibility de récupérer pas l'espace qui reste vide
        getView().findViewById(R.id.lastUpdatePhotos).setVisibility(lastUpdatePhotosTime != null ? View.VISIBLE : View.GONE);
        if (lastUpdatePhotosTime != null) {
            String lastUpdatePhotoTime = lastUpdatePhotosTime != null ? sdf.format(lastUpdatePhotosTime) : null;
            TextView textView = getView().findViewById(R.id.lastUpdatePhotosTime);
            textView.setText(lastUpdatePhotoTime);
        }
        getView().findViewById(R.id.lastSync).setVisibility(lastSyncTime != null ? View.VISIBLE : View.GONE);
        if (lastSyncTime != null) {
            String stringLastSyncTime = lastSyncTime != null ? sdf.format(lastSyncTime) : null;
            TextView textView = getView().findViewById(R.id.lastSyncTime);
            textView.setText(stringLastSyncTime);
        }
        getView().findViewById(R.id.lastWallpaper).setVisibility(lastWallpaperTime != null ? View.VISIBLE : View.GONE);
        if (lastWallpaperTime != null) {
            String stringLastWallpaperTime = lastWallpaperTime != null ? sdf.format(lastWallpaperTime) : null;
            TextView textView = getView().findViewById(R.id.lastWallpaperTime);
            textView.setText(stringLastWallpaperTime);
        }
    }

    @Override
    public void updateUI_Folder(String humanPath) {
        TextView textView = getView().findViewById(R.id.textFolder);
        textView.setText(humanPath);
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.error))
                .setMessage(message)
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void setStat(WallpaperStat stat) {
        TextView view = getView().findViewById(R.id.stat);
        String text = new UITextHelper((MainActivity) getActivity()).getStat(stat);
        view.setText(text);
    }

    @Override
    public void setWarningWallpaperActive(boolean active) {
        TextView view = getView().findViewById(R.id.warning_wallpaper_active);
        view.setVisibility(active ? View.GONE : View.VISIBLE);
    }
}
