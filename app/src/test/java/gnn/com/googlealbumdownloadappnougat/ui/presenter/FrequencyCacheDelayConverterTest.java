package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import gnn.com.googlealbumdownloadappnougat.Frequency;
import gnn.com.photos.service.Cache;
import gnn.com.photos.sync.SynchronizerDelayed;

public class FrequencyCacheDelayConverterTest {

    @Test
    public void getFrequencyUpdatePhotosHour() {
        int result = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosDayHour(10);

        assertThat(result, is(10 * 24));
    }

    @Test
    public void conversion_with_never() {
        // when
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosDayHour(-1);
        int resultSync = FrequencyCacheDelayConverter.getFrequencySyncHourMinute(-1);

        // then
        assertThat(resultUpdate, is(SynchronizerDelayed.DELAY_NEVER_SYNC));
        assertThat(resultSync, is(Cache.DELAY_NEVER_EXPIRE));
    }

    @Test
    public void conversion_with_always() {
        // when
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosDayHour(0);
        int resultSync = FrequencyCacheDelayConverter.getFrequencySyncHourMinute(0);
        // then
        assertThat(resultUpdate, is(SynchronizerDelayed.DELAY_ALWAYS_SYNC));
        assertThat(resultSync, is(Cache.DELAY_ALWAYS_EXPIRE));
    }

    @Test
    public void conversion_with_max() {
        // when
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosDayHour(Integer.MAX_VALUE);
        int resultSync = FrequencyCacheDelayConverter.getFrequencySyncHourMinute(Integer.MAX_VALUE);
        // then
        assertThat(resultUpdate, is(Integer.MAX_VALUE));
        assertThat(resultSync, is(Integer.MAX_VALUE));
    }

    @Test
    public void conversion_with_normal() {
        // when
        int resultSync = FrequencyCacheDelayConverter.getFrequencySyncHourMinute(720);
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosDayHour(168);
        // then
        assertThat(resultSync, is(720* 60));
        assertThat(resultUpdate, is(168 * 24));
    }

    @Test
    public void conversion_with_normal_hourhour() {
        // when
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosHourHour(168);
        // then
        assertThat(resultUpdate, is(168));
    }

    @Test
    public void conversion_min_hourhour() {
        // when
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosHourHour(Frequency.NEVER);
        // then
        assertThat(resultUpdate, is(Cache.DELAY_NEVER_EXPIRE));
    }

    @Test
    public void conversion_max_hourhour() {
        // when
        int resultUpdate = FrequencyCacheDelayConverter.getFrequencyUpdatePhotosHourHour(Frequency.ALWAYS);
        // then
        assertThat(resultUpdate, is(Cache.DELAY_ALWAYS_EXPIRE));
    }

}