package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.net.Uri;

class Folder {

    static String getHumanPath(Uri uri) {
        final String[] split = uri.getLastPathSegment().split(":");
        return split[1];
    }

}
