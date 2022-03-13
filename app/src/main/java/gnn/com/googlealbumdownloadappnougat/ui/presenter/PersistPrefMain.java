package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    public static final int DEF_FREQ_WALLPAPER_MINUTE = 60;
    public static final int DEF_FREQ_SYNC_HOUR = 72;
    public static final int DEF_FREQ_UPDATE_PHOTO_DAY = 30;

    private final Context activity;

    public PersistPrefMain(Context activity) {
        // Presenter is injected as methods parameter
        // because Presenter can be PresenterMain or PresenterSettings
        this.activity = activity;
    }

    /**
     * Get data from UI throught Persenter and store then into preferences
     * @param presenter
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

    public void saveFrequencies(IPresenterFrequencies presenter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();

        int frequencyWallpaper = presenter.getFrequencyWallpaper();
        int frequencySync = presenter.getFrequencySync();
        int frequencyUpdatePhotos = presenter.getFrequencyUpdatePhotos();

        editor.putInt(PREF_FREQ_WALLPAPER, frequencyWallpaper);
        editor.putInt(PREF_FREQ_SYNC, frequencySync);
        editor.putInt(PREF_FREQ_UPDATE_PHOTOS, frequencyUpdatePhotos);

        editor.apply();
    }

    /**
     * retrieves data from Preferences and inject into Presenter
     * @param presenter
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
        presenter.setFrequencySync(data.getFrequencySync());
        presenter.setFrequencyWallpaper(data.getFrequencyWallpaper());
    }

    /**
     * Get Data from Preferences and return them as SyncData.
     * Used to restore main UI and from schedule UI to enqueue work
     * UI Unit and Préference Unit are the same
     * @return
     */
    private SyncData getData() {
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

    /**
     * @return fréquence en minute
     */
    public int getFrequencyUpdatePhotosHour() {
        return getData().getFrequencyUpdatePhotos() * 24 * 60;
    }

    public String getAlbum() {
        return getData().getAlbum();
    }
}
