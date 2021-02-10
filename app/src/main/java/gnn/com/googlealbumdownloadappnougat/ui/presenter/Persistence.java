package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Default values are taken from Presenter
 */
public class Persistence {

    private static final String PREF_ALBUM_KEY = "album";
    private static final String PREF_FOLDER_HUMAN_KEY = "folder_human";
    private static final String PREF_QUANTITY_KEY = "quantity";
    private static final String PREF_CACHE_MAX_AGE = "cache_max_age";

    private final Activity activity;

    public Persistence(Activity activity) {
        this.activity = activity;
    }

    public void saveData(IPresenterMain presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        String album = presenter.getAlbum();
        String folderHuman = presenter.getFolderHuman();
        int quantity = presenter.getQuantity();
        editor.putString(PREF_ALBUM_KEY, album);
        editor.putString(PREF_FOLDER_HUMAN_KEY, folderHuman);
        editor.putInt(PREF_QUANTITY_KEY, quantity);
        editor.apply();
    }

    /**
     * retrieves data from Preferences and inject into Presenter
     */
    public void restoreData(IPresenterMain presenter) {
        // Restore data
        // default values are taken from presenter
        // presenter.setXXX update TextViews
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
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

    public void saveSettings(IPresenterSettings presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREF_CACHE_MAX_AGE, presenter.getCacheMaxAge());
        editor.apply();
    }

    public void restoreSettings(IPresenterSettings presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
            long cacheMaxAge = preferences.getLong(PREF_CACHE_MAX_AGE, PresenterSettings.DefaultCacheMaxAge);
            presenter.setCacheMaxAge(cacheMaxAge);
        }
    }
}
