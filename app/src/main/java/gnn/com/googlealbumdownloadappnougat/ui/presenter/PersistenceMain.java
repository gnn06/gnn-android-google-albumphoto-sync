package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Default values are taken from Presenter
 */
public class PersistenceMain {

    private static final String PREF_ALBUM_KEY = "album";
    private static final String PREF_FOLDER_HUMAN_KEY = "folder_human";
    private static final String PREF_QUANTITY_KEY = "quantity";
    private static final String PREF_RENAME = "rename";

    private final Activity activity;

    public PersistenceMain(Activity activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    public void save(IPresenterMain presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        String album = presenter.getAlbum();
        String folderHuman = presenter.getFolderHuman();
        int quantity = presenter.getQuantity();
        String rename = presenter.getRename();

        editor.putString(PREF_ALBUM_KEY, album);
        editor.putString(PREF_FOLDER_HUMAN_KEY, folderHuman);
        editor.putInt(PREF_QUANTITY_KEY, quantity);
        editor.putString(PREF_RENAME, rename);

        editor.apply();
    }

    /**
     * retrieves data from Preferences and inject into Presenter
     * @param presenter
     */
    public void restore(IPresenterMain presenter) {
        // Restore data
        SyncData data = getData();
        if (data.getAlbum() != null) {
            presenter.setAlbum(data.getAlbum());
        }
        if (data.getFolderHuman() != null) {
            presenter.setFolderHuman(data.getFolderHuman());
        }
        presenter.setQuantity(data.getQuantity());
        presenter.setRename(data.getRename());
    }

    public SyncData getData() {
        SyncData data = new SyncData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
            String album = preferences.getString(PREF_ALBUM_KEY, null);
            if (album != null) {
                data.setAlbum(album);
            }
            String folderHuman = preferences.getString(PREF_FOLDER_HUMAN_KEY, null);
            if (folderHuman != null) {
                data.setFolderHuman(folderHuman);
            }
            int quantity = preferences.getInt(PREF_QUANTITY_KEY, -1);
            data.setQuantity(quantity);
            String rename = preferences.getString(PREF_RENAME, null);
            data.setRename(rename);
        }
        return data;
    }

}