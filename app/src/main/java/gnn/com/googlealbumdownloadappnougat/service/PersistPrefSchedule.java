package gnn.com.googlealbumdownloadappnougat.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PersistPrefSchedule {

    private static final String PREF_INTERVAL = "interval";

    private final ActivitySchedule activity;

    public PersistPrefSchedule(ActivitySchedule activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    public void save(IPresenterSchedule presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_INTERVAL, presenter.getIntervalSync());
        editor.apply();
    }

    public void restore(IPresenterSchedule presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
            int interval = preferences.getInt(PREF_INTERVAL, PresenterSchedule.DefaultInterval);
            presenter.setIntervalSync(interval);
        }
    }
}
