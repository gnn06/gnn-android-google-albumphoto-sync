package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import gnn.com.googlealbumdownloadappnougat.wizard.WizardStep;

/**
 * Default values are taken from Presenter
 */
public class PersistPrefMain {

    // TODO create default values

    private static final String PREF_ALBUM_KEY = "album";
    private static final String PREF_FOLDER_HUMAN_KEY = "folder_human";
    private static final String PREF_QUANTITY_KEY = "quantity";
    private static final String PREF_RENAME = "rename";
    private static final String PREF_FREQ_WALLPAPER = "frequency_wallpaper";
    private static final String PREF_FREQ_SYNC = "frequency_sync";
    private static final String PREF_FREQ_UPDATE_PHOTOS = "frequency_update_photos";
    private static final String PREF_WIZARD_STEP = "wizard_step";
    private static final String PREF_WIZARD_ACTIVE = "wizard_active";

    public static final int DEF_FREQ_WALLPAPER_MINUTE = 60;
    public static final int DEF_FREQ_SYNC_HOUR = 72;
    public static final int DEF_FREQ_UPDATE_PHOTO_DAY = 30;
    public static final WizardStep DEF_WIZARD_STEP = WizardStep.S00_NOT_STARTED;
    public static final boolean DEF_WIZARD_ACTIVE = false;

    private final Context activity;

    public PersistPrefMain(Context activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    /**
     * Get data from UI throught Persenter and store then into preferences
     */
    public void save(IPresenterHome presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        String album = presenter.getAlbum();
        String folderHuman = presenter.getDefaultFolderHuman();

        editor.putString(PREF_ALBUM_KEY, album);
        editor.putString(PREF_FOLDER_HUMAN_KEY, folderHuman);

        editor.apply();
    }

    public void saveDownloadOption(IPresenterDownloadOptions presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        int quantity = presenter.getQuantity();
        String rename = presenter.getRename();

        editor.putInt(PREF_QUANTITY_KEY, quantity);
        editor.putString(PREF_RENAME, rename);

        editor.apply();
    }

    public void saveFrequencies(int frequencyWallpaper, int frequencySync, int frequencyUpdatePhotos) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREF_FREQ_WALLPAPER, frequencyWallpaper);
        editor.putInt(PREF_FREQ_SYNC, frequencySync);
        editor.putInt(PREF_FREQ_UPDATE_PHOTOS, frequencyUpdatePhotos);

        editor.apply();
    }

    /**
     * retrieves data from Preferences and inject into Presenter
     */
    public void restore(IPresenterHome presenter) {
        // Restore data
        SyncData data = getData();
        if (data.getAlbum() != null) {
            presenter.setAlbum(data.getAlbum());
        }
        if (data.getFolderHuman() != null) {
            presenter.setDefaultFolderHuman(data.getFolderHuman());
        }
    }

    public void restoreDownloadOptions(IPresenterDownloadOptions presenter) {
        // Restore data
        SyncData data = getData();
        presenter.setQuantity(data.getQuantity());
        presenter.setRename(data.getRename());
    }

    public void restoreFrequencies(IPresenterFrequencies presenter) {
        SyncData data = getData();
        presenter.setFrequencyUpdatePhotos(data.getFrequencyUpdatePhotos());
        presenter.setFrequencySyncHour(data.getFrequencySync());
        presenter.setFrequencyWallpaper(data.getFrequencyWallpaper());
    }

    /**
     * Get Data from Preferences and return them as SyncData.
     * Used to restore main UI and from schedule UI to enqueue work
     * UI Unit and Préference Unit are the same
     * If no data are préviously saved, return default values
     */
    SyncData getData() {
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
            int frequencyWallpaper = preferences.getInt(PREF_FREQ_WALLPAPER, DEF_FREQ_WALLPAPER_MINUTE);
            data.setFrequencyWallpaper(frequencyWallpaper);
            int frequencySync = preferences.getInt(PREF_FREQ_SYNC, DEF_FREQ_SYNC_HOUR);
            data.setFrequencySync(frequencySync);
            int frequencyUpdatePhotos = preferences.getInt(PREF_FREQ_UPDATE_PHOTOS, DEF_FREQ_UPDATE_PHOTO_DAY);
            data.setFrequencyUpdatePhotos(frequencyUpdatePhotos);
        }
        return data;
    }

    public String getPhotoPath() {
        return getData().getFolderHuman();
    }

    public int getQuantity() {
        return getData().getQuantity();
    }

    public String getRename() {
        return getData().getRename();
    }

    public int getFrequencyUpdatePhotos() {
        return getData().getFrequencyUpdatePhotos();
    }

    public int getFrequencyDownload() {
        return getData().getFrequencySync();
    }

    public int getFrequencyWallpaper() {
        return getData().getFrequencyWallpaper();
    }

    public String getAlbum() {
        return getData().getAlbum();
    }

    public void saveWizardStep(WizardStep step) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PREF_WIZARD_STEP, step.name());

        editor.apply();
    }

    public WizardStep restoreWizardStep() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
            try {
                return WizardStep.valueOf(preferences.getString(PREF_WIZARD_STEP, DEF_WIZARD_STEP.name()));
            } catch (IllegalArgumentException e) {
                return WizardStep.S00_NOT_STARTED;
            }
        }
        return DEF_WIZARD_STEP;
    }

    public void saveWizardActive(boolean isActive) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(PREF_WIZARD_ACTIVE, isActive);

        editor.apply();
    }

    public boolean restoreWizardActive() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences != null) {
            boolean isActive = preferences.getBoolean(PREF_WIZARD_ACTIVE, DEF_WIZARD_ACTIVE);
            return isActive;
        }
        return DEF_WIZARD_ACTIVE;
    }
}
