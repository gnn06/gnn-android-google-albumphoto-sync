package gnn.com.googlealbumdownloadappnougat.util;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import android.content.Context;

public class AppLogger {

    private static final String TAG = "AppLogger";

    public static Logger getLogger(Context context) {
        Logger logger = Logger.getLogger("worker");
        Handler[] handlers = logger.getHandlers();
        if (handlers.length == 0) {
            try {
//                Handler handler = new FileHandler("/tmp/dev/toto.log");
                File cacheDir = context.getCacheDir();
                Handler handler = new FileHandler(cacheDir.getAbsolutePath() + "/app.log");
                Formatter formatter = new SimpleFormatter();
                handler.setFormatter(formatter);
                logger.addHandler(handler);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return logger;
    }
}
