package gnn.com.googlealbumdownloadappnougat.presenter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.io.File;

import gnn.com.googlealbumdownloadappnougat.MainActivity;

public interface IPresenter {

    void onShowAlbumList();

    void onChooseAlbum(String albumName);

    void handleSignInResult(Task<GoogleSignInAccount> completedTask, MainActivity mainActivity);

    void onSyncClick();

    void laucnhSync();

    void launchSynchWithPermission();

    File getFolder();
}
