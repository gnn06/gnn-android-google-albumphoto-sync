package gnn.com.googlealbumdownloadappnougat.wallpaper.stat;

import static org.junit.Assert.*;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.Date;

import gnn.com.googlealbumdownloadappnougat.util.DateProvider;

public class StatUpdaterTest {
    @Test
    public void updateOnChange_on_same_day() {
        StatUpdater updater = new StatUpdater(new DateProvider(){
            @Override
            public Date get() {
                return new Date(2022, 5, 12, 18, 0);
            }
        });
        WallpaperStat stat = new WallpaperStat(12, new Date(2022, 5, 12, 17, 32));
        updater.updateOnNewChange(stat);
        assertThat(stat.getNbChangeOnLastDay(), Is.is(13));
    }

    @Test
    public void updateOnChange_on_other_day() {
        StatUpdater updater = new StatUpdater(new DateProvider(){
            @Override
            public Date get() {
                return new Date(2022, 5, 12, 18, 0);
            }
        });
        WallpaperStat stat = new WallpaperStat(12, new Date(2022, 5, 10, 17, 32));
        updater.updateOnNewChange(stat);
        assertThat(stat.getNbChangeOnLastDay(), Is.is(1));
    }
}