package gnn.com.googlealbumdownloadappnougat.service;

import androidx.work.WorkInfo;

public interface IScheduleView {
    void setStateSync(WorkInfo state);

    void setStateWallpaper(WorkInfo stateWallpaper);
}
