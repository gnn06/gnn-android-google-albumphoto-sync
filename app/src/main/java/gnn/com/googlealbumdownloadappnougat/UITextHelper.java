package gnn.com.googlealbumdownloadappnougat;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;
import gnn.com.googlealbumdownloadappnougat.wallpaper.stat.WallpaperStat;
import gnn.com.photos.sync.Synchronizer;

public class UITextHelper {

    private final MainActivity activity;

    public UITextHelper(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    /**
     * @param lastSyncTime can be null
     * @param lastWallpaperTime
     */
    String getLastTimesString(String lastSyncTime, String lastWallpaperTime) {
        String result = "";
        result = lastSyncTime == null ?
                activity.getResources().getString(R.string.no_previous_sync)
                : activity.getResources().getString(R.string.last_sync_time, lastSyncTime);
        if (lastWallpaperTime != null) {
            result += "\n" + activity.getResources().getString(R.string.last_wallpaper_time, lastWallpaperTime);
        }
        return result;
    }

    String getResultString(Synchronizer synchronizer, SyncStep step, MainActivity mainActivity) {
        String result = "";
        switch (step) {
            case STARTING:
                result += activity.getResources().getString(R.string.sync_starting);
                break;
            case IN_PRORGESS:
                result += activity.getResources().getString(R.string.sync_in_progress) + "\n";
                result += getDetailResultText(synchronizer, false);
                break;
            case FINISHED:
                result += activity.getResources().getString(R.string.sync_finished) + "\n";
                result += getDetailResultText(synchronizer, true);
                break;
        }
        return result;
    }

    String getDetailResultText(Synchronizer synchronizer, boolean finished) {
        String result = "";
        result += "album size = " + synchronizer.getAlbumSize() + "\n";
        result += "downloaded = ";
        if (!finished) {
            result += synchronizer.getCurrentDownload() + " / ";
        }
        result += synchronizer.getTotalDownload();
        result += "\n";
        result += "deleted = ";
        if (!finished) {
            result += synchronizer.getCurrentDelete() + " / ";
        }
        result += synchronizer.getTotalDelete();
        return result;
    }

    public String getStat(WallpaperStat stat) {
        String result = "";

        result += "nombre de changement " + stat.getNbChangeOnLastDay() + " le "
        + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE).format(stat.getLastChangeDate());
        return result;
    }
}