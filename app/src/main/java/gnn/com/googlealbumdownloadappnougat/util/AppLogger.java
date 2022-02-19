package gnn.com.googlealbumdownloadappnougat.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {

    private static final String TAG = "AppLogger";

    public static Logger getLogger(Context context) {
        return getLogger(context.getCacheDir().getAbsolutePath());
    }

    private static Logger getLogger(String path) {
        Logger logger = Logger.getLogger("worker");
        logger.setLevel(Level.FINEST);
        Handler[] handlers = logger.getHandlers();
        if (handlers.length == 0) {
            try {
                Handler handler = new FileHandler(path + "/app.log", true);
                handler.setLevel(Level.FINEST);
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
