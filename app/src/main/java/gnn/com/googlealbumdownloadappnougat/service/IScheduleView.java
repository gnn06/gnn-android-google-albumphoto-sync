package gnn.com.googlealbumdownloadappnougat.service;

import androidx.work.WorkInfo;

public interface IScheduleView {
    void setIntervalSync(int interval);

    int getIntervalSync();

    void setState(WorkInfo state);
}
