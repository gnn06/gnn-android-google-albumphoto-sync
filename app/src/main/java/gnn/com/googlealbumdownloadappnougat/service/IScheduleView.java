package gnn.com.googlealbumdownloadappnougat.service;

import androidx.work.WorkInfo;

public interface IScheduleView {
    void setInterval(int interval);

    int getInterval();

    void setState(WorkInfo state);
}
