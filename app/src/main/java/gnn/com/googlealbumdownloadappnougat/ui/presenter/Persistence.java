package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Default values are taken from Presenter
 */
public class Persistence {

    private static final String PREF_ALBUM_KEY = "album";
    private static final String PREF_FOLDER_HUMAN_KEY = "folder_human";
    private static final String PREF_QUANTITY_KEY = "quantity";

    private Activity activity;
    private IPresenter presenter;

    public Persistence(Activity activity, IPresenter presenter) {
        this.activity = activity;
        this.presenter = presenter;
    }

    public void saveData() {
        SharedPreferences preferences = this.activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String album = presenter.getAlbum();
        String folderHuman = presenter.getFolderHuman();
        int quantity = presenter.getQuantity();
        editor.putString(PREF_ALBUM_KEY, album);
        editor.putString(PREF_FOLDER_HUMAN_KEY, folderHuman);
        editor.putInt(PREF_QUANTITY_KEY, quantity);
        editor.commit();
    }

    public void restoreData() {
        // Restore data
        // default values are taken from presenter
        // presenter.setXXX update TextViews
        SharedPreferences preferences = this.activity.getPreferences(Context.MODE_PRIVATE);
        if (preferences != null) {
            String album = preferences.getString(PREF_ALBUM_KEY, presenter.getAlbum());
            if (album != null) {
                presenter.setAlbum(album);
            }
            String folderHuman = preferences.getString(PREF_FOLDER_HUMAN_KEY, presenter.getFolderHuman());
            if (folderHuman != null) {
                presenter.setFolderHuman(folderHuman);
            }
            int quantity = preferences.getInt(PREF_QUANTITY_KEY, -1);
            presenter.setQuantity(quantity);
        }
    }
}
