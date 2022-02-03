package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class WallpaperStat {

    private int nbChangeOnLastDay = 0;

    private Date lastChangeDate = null;

    public WallpaperStat(int changeByDay, Date lastWallpaper) {
        this.nbChangeOnLastDay = changeByDay;
        this.lastChangeDate = lastWallpaper;
    }

    public int getNbChangeOnLastDay() {
        return nbChangeOnLastDay;
    }

    private void setNbChangeOnLastDay(int nbChangeOnLastDay) {
        this.nbChangeOnLastDay = nbChangeOnLastDay;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }

    private void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    void updateOnNewChange() {
        Date updateDate = new Date();
        Instant instantNewChange = updateDate.toInstant().truncatedTo(ChronoUnit.CENTURIES);
        Instant instantLastChange = this.lastChangeDate.toInstant().truncatedTo(ChronoUnit.DAYS);
        if (instantNewChange == instantLastChange) {
            this.nbChangeOnLastDay += 1;
        } else {
            this.nbChangeOnLastDay = 0;
        }
        this.lastChangeDate = updateDate;
    }
}
