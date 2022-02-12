package gnn.com.photos.sync;

import java.util.Date;

import gnn.com.photos.Photo;

public class PhotoChoose {

    final public Photo photo;
    final public Date chooseDate;

    public PhotoChoose(Photo photo, Date chooseDate1) {
        this.photo = photo;
        this.chooseDate = chooseDate1;
    }
}
