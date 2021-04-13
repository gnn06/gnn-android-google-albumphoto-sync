package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Default values are taken from Presenter
 */
public class PersistenceSettings {

    private static final String PREF_CACHE_MAX_AGE = "cache_max_age";

    private final Activity activity;

    public PersistenceSettings(Activity activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    public void save(IPresenterSettings presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PREF_CACHE_MAX_AGE, presenter.getCacheMaxAge());
        editor.apply();
    }

    public void restore(IPresenterSettings presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
            long cacheMaxAge = preferences.getLong(PREF_CACHE_MAX_AGE, PresenterSettings.DefaultCacheMaxAge);
            presenter.setCacheMaxAge(cacheMaxAge);
        }
    }
}
