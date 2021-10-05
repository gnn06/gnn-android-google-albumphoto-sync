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
    private static final String PREF_FREQ_WALLPAPER = "frequency_wallpaper";
    private static final String PREF_FREQ_SYNC = "frequency_sync";

    private final Activity activity;

    public PersistenceMain(Activity activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    /**
     * Get data from UI throught Persenter and store then into preferences
     * @param presenter
     */
    public void save(IPresenterMain presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        String album = presenter.getAlbum();
        String folderHuman = presenter.getFolderHuman();
        int quantity = presenter.getQuantity();
        String rename = presenter.getRename();
        int frequencyWallpaper = presenter.getFrequencyWallpaper();
        int frequencySync = presenter.getFrequencySync();

        editor.putString(PREF_ALBUM_KEY, album);
        editor.putString(PREF_FOLDER_HUMAN_KEY, folderHuman);
        editor.putInt(PREF_QUANTITY_KEY, quantity);
        editor.putString(PREF_RENAME, rename);
        editor.putInt(PREF_FREQ_WALLPAPER, frequencyWallpaper);
        editor.putInt(PREF_FREQ_SYNC, frequencySync);

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
        presenter.setFrequencyWallpaper(data.getFrequencyWallpaper());
        presenter.setFrequencySync(data.getFrequencySync());
    }

    /**
     * Get Data from Preferences and return them as SyncData.
     * Used to restore main UI and from schedule UI to enqueue work
     * @return
     */
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
            int frequencyWallpaper = preferences.getInt(PREF_FREQ_WALLPAPER, -1);
            data.setFrequencyWallpaper(frequencyWallpaper);
            int frequencySync = preferences.getInt(PREF_FREQ_SYNC, -1);
            data.setFrequencySync(frequencySync);
        }
        return data;
    }

}
