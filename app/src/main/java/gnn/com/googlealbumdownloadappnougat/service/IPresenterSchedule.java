package gnn.com.googlealbumdownloadappnougat.service;

public interface IPresenterSchedule {
    void onInit();

    void onScheduleSync();

    void onCancelSync();

    int getIntervalSync();

    void setIntervalSync(int interval);
}
