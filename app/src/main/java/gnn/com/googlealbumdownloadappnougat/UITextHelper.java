package gnn.com.googlealbumdownloadappnougat;

import java.text.DateFormat;
import java.util.Locale;

import gnn.com.photos.stat.stat.WallpaperStat;
import gnn.com.photos.sync.SyncData;

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

    public String getResultString(SyncData syncData, SyncStep step, MainActivity mainActivity) {
        if (syncData == null) {
            return activity.getResources().getString(R.string.no_result);
        }
        String result = "";
        switch (step) {
            case STARTING:
                result += activity.getResources().getString(R.string.sync_starting);
                break;
            case IN_PRORGESS:
                result += activity.getResources().getString(R.string.sync_in_progress) + "\n";
                result += getDetailResultText(syncData, false);
                break;
            case FINISHED:
                result += activity.getResources().getString(R.string.sync_finished) + "\n";
                result += getDetailResultText(syncData, true);
                break;
        }
        return result;
    }

    String getDetailResultText(SyncData syncData, boolean finished) {
        if (syncData == null) return "";
        String result = "";
        // when SyncData is restored, number != null && list == null
        int totalToDelete = 0;
        int totalToDownload = 0;

        if (syncData.getToDelete() != null)
            totalToDelete = syncData.getToDelete().size();
        else
            totalToDelete = syncData.getDeleteCount();

        if (syncData.getToDownload() != null)
            totalToDownload = syncData.getToDownload().size();
        else
            totalToDownload = syncData.getDownloadCount();

        if (totalToDelete == 0 && totalToDownload == 0)
            return result;

        result += "album size = " + syncData.getAlbumSize() + "\n";
        result += "downloaded = ";
        if (!finished) {
            result += syncData.getCurrentDownload() + " / ";
        }
        result += totalToDownload;
        result += "\n";
        result += "deleted = ";
        if (!finished) {
            result += syncData.getCurrentDelete() + " / ";
        }
        result += totalToDelete;
        return result;
    }

    public String getStat(WallpaperStat stat) {
        String result = "";
        if (stat != null && stat.getDate() != null) {
            result += "nombre de changement " + stat.getChangeOnDayBefore() + " la veille du "
                    + DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE).format(stat.getDate());
        } else {
            result = "Aucune stat.";
        }
        return result;
    }
}