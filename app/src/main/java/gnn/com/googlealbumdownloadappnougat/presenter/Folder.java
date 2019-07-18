package gnn.com.googlealbumdownloadappnougat.presenter;

import android.net.Uri;

class Folder {

    static String getHumanPath(Uri uri) {
        final String[] split = uri.getLastPathSegment().split(":");
        final String humanPath = split[1];
        return humanPath;
    }

}
