package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.SyncStep;
import gnn.com.googlealbumdownloadappnougat.UITextHelper;
import gnn.com.googlealbumdownloadappnougat.ui.FolderModel;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewHome;
import gnn.com.googlealbumdownloadappnougat.util.Logger;
import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.sync.SyncData;

public class FragmentHome extends FragmentHighlight implements IViewHome {

    private static final String TAG = "FRAGMENTHOME";
    private IPresenterHome presenter;
    private FolderModel folderModel;

    public FragmentHome() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new PresenterHome((MainActivity) getActivity(), this);
        getView().findViewById(R.id.SectionUser).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onSignIn();
            }
        });
        view.findViewById(R.id.sectionDownloadOptions).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(getActivity(), R.id.fragment_container_view);
                controller.navigate(R.id.action_FragmentHome_to_FragmentDownloadOptions);
            }
        });
        getView().findViewById(R.id.SectionAlbum).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onShowAlbumList();
            }
        });

        getView().findViewById(R.id.SectionFolder).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onChooseFolder();
            }
        });

        getView().findViewById(R.id.ChooseOneButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onButtonSyncOnce();
            }
        });

        getView().findViewById(R.id.ButtonWallpaper).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onButtonWallpaperOnce();
            }
        });

        getView().findViewById(R.id.warning_wallpaper_active).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presenter.onWarningWallpaperActive();
            }
        });

        final UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class);
        userModel.getUser().observe(getActivity(), new Observer<GoogleSignInAccount>() {
            @Override
            public void onChanged(@Nullable GoogleSignInAccount account) {
                updateUI_User(account);
            }
        });

        this.folderModel = new ViewModelProvider(requireActivity()).get(FolderModel.class);
        folderModel.getFolder().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String folder) {
                updateUI_Folder(folder);
            }
        });
        presenter.onAppStart();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static final int RC_CHOOSE_FOLDER = 504;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult, requestCode=" + requestCode + ", resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_CHOOSE_FOLDER && resultCode == MainActivity.RESULT_OK) {
            // return to app without choosing a folder : data == null && resultCode == 0
            // data.getDate.Uri = "content:/.....Pictures%2Fwallpaper"
            presenter.setFolder(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onAppForeground();
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.getLogger().fine("onPause");
        PersistPrefMain preferences = new PersistPrefMain(getActivity());
        preferences.save(presenter);
        // not necessary to save settings as settings can not be changed in this activity
    }

    @Override
    public void onAlbumChosenResult(String albumName) {
        TextView textView = getView().findViewById(R.id.textAlbum);
        textView.setText(albumName);
    }

    public void showChooseAlbumDialog(final ArrayList<String> mAlbums) {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mAlbums);
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
        updateUI_User(account);
    }

    public void updateUI_User(GoogleSignInAccount account) {
        String name;
        if (account != null) {
            name = account.getEmail();
//            String GoogleAuthorization = account.getGrantedScopes().toString();
//            name += "\nGoogle permission=" + GoogleAuthorization;
//            String writeAuthorization;
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//                writeAuthorization = "write";
//            else writeAuthorization = "no write";
//            name += "\n" + GoogleAuthorization + ", " + writeAuthorization;
//            name += "\nIsExpired() = " + account.isExpired();
        } else {
            name = getResources().getString(R.string.login);
        }

        if (getView() != null) {
            TextView myAwesomeTextView = getView().findViewById(R.id.textUser);
            myAwesomeTextView.setText(name);
        }

        Log.d(TAG, "updateUI_User, account=" + (account == null ? "null" : account.getEmail()));
    }

    @Override
    public void updateUI_CallResult(SyncData syncData, SyncStep step) {
        String result = new UITextHelper((MainActivity) getActivity()).getResultString(syncData, step, (MainActivity) getActivity());
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

    /**
     * @param humanPath "Pictures/wallpaper"
     */
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
