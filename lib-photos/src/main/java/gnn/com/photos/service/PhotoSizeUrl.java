package gnn.com.photos.service;

import java.net.MalformedURLException;
import java.net.URL;

import gnn.com.util.ScreenSize;

public class PhotoSizeUrl {
    URL getUrl(URL baseUrl, ScreenSize screenSize) {
        // =w2048-h1024
        // if asked width is greater than photo then doanloaded size = photo size
        int width = screenSize.width;
        int height = screenSize.height;
        if (width > 0 && height > 0) {
            try {
                int max = Math.max(width, height);
                return new URL(baseUrl.toString() + "=w" + max + "-h" + max);
            } catch (MalformedURLException e) {
                return baseUrl;
            }

        }
        return baseUrl;
    }
}
