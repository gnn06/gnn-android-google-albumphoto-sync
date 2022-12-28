package gnn.com.googlealbumdownloadappnougat.service;

import java.time.LocalDateTime;

class Item {
    public Item(State state) {
        String dateTime = LocalDateTime.now().toString();
    }

    enum State {
        SUCCESS, FAILURE
    }
}
