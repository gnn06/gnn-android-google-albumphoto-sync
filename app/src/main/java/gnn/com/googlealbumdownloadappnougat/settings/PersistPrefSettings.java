package gnn.com.googlealbumdownloadappnougat.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import gnn.com.googlealbumdownloadappnougat.settings.IPresenterSettings;
import gnn.com.googlealbumdownloadappnougat.settings.PresenterSettings;

/**
 * Default values are taken from Presenter
 */
public class PersistPrefSettings {

    private static final String PREF_CACHE_MAX_AGE = "cache_max_age";

    private final Activity activity;

    public PersistPrefSettings(Activity activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    public void save(IPresenterSettings presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.apply();
    }

    public void restore(IPresenterSettings presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
        }
    }
}
