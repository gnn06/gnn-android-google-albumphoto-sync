package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;

public class PresenterFrequenciesIntTest {

    // Under Test
    private PresenterFrequencies presenter;

    // Dependencies
    private IViewFrequencies view;
    private Context context;
    private PersistPrefMain persist;
    private ScheduleFromFreq schedulerFromFreq;

    @Before
    public void setUp() throws Exception {
        view = mock(FragmentFrequencies.class);
        context = null;
        persist = mock(PersistPrefMain.class);
        schedulerFromFreq = mock(ScheduleFromFreq.class);

        presenter = new PresenterFrequencies(view, context, persist, schedulerFromFreq);
    }

    @Test
    public void frequenceUpdateChange_presist_schedule() {
        InOrder inOrder = inOrder(persist, schedulerFromFreq);
        // When
        presenter.setFrequencyUpdateWithSchedule(720);
        // Then
        // Verify that persist was called before scheduling
        inOrder.verify(persist).saveFrequencies(anyInt(), anyInt(), eq(720));
        inOrder.verify(schedulerFromFreq).scheduleOrCancel();
    }

    @Test
    public void frequenceSyncChange_presist_schedule() {
        InOrder inOrder = inOrder(persist, schedulerFromFreq);
        // When
        presenter.setFrequencySyncWithSchedule(120);
        // Then
        // Verify that persist was called before scheduling
        inOrder.verify(persist).saveFrequencies(anyInt(), eq(120), anyInt());
        inOrder.verify(schedulerFromFreq).scheduleOrCancel();
    }

    @Test
    public void frequenceWallpaperChange_presist_schedule() {
        InOrder inOrder = inOrder(persist, schedulerFromFreq);
        // When
        presenter.setFrequencyWallpaperWithSchedule(1);
        // Then
        // Verify that persist was called before scheduling
        inOrder.verify(persist).saveFrequencies(eq(1), anyInt(), anyInt());
        inOrder.verify(schedulerFromFreq).scheduleOrCancel();
    }
}