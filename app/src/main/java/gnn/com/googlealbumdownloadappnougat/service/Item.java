package gnn.com.googlealbumdownloadappnougat.service;

import java.time.LocalDateTime;

class Item {
    private final State state;
    private final String dateTime;
    public Item(State state) {
        this.state = state;
        this.dateTime = LocalDateTime.now().toString();
    }

    enum State {
        SUCCESS, FAILURE;
    }
}
