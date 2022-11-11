package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.SynchronizerDelayed;

public class FrequencyCacheDelayConverter {

    /**
     * int frequencySync fréquence de téléchargement en minute
     * @param frequencyHour
     */
    static public int getFrequencySyncHourMinute(int frequencyHour) {
        if (frequencyHour == Frequency.NEVER)
            return SynchronizerDelayed.DELAY_NEVER_SYNC;
        else if (frequencyHour == 0) {
            return SynchronizerDelayed.DELAY_ALWAYS_SYNC;
        } else if (frequencyHour < Integer.MAX_VALUE)
            return frequencyHour * 60;
        else
            return Integer.MAX_VALUE;
    }

    /**
     * @return fréquence in hour
     * @param frequencyDay
     */
    static public int getFrequencyUpdatePhotosDayHour(int frequencyDay) {
        if (frequencyDay == Frequency.NEVER)
            return Cache.DELAY_NEVER_EXPIRE;
        else if (frequencyDay == 0) {
            return Cache.DELAY_ALWAYS_EXPIRE;
        } else if (frequencyDay < Integer.MAX_VALUE)
            return frequencyDay * 24;
        else
            return Integer.MAX_VALUE;
    }

    /**
     * @return fréquence in hour
     * @param frequencyHour
     */
    static public int getFrequencyUpdatePhotosHourHour(int frequencyHour) {
        if (frequencyHour == Frequency.NEVER)
            return Cache.DELAY_NEVER_EXPIRE;
        else if (frequencyHour == Frequency.ALWAYS) {
            return Cache.DELAY_ALWAYS_EXPIRE;
        } else {
            return frequencyHour;
        }
    }
}