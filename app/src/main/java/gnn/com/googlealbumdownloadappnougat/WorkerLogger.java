package gnn.com.googlealbumdownloadappnougat;

import android.util.Log;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class WorkerLogger {

    private static final String TAG = "WorkerLogger";
    private static final String LOG_FILENAME = "MyWorker";

    private final Logger logger;

    WorkerLogger(String path) {
        this.logger = getLogger(path);
    }

    void log(Level level, String message) {
        if (logger != null) {
            logger.log(level, message);
        }
    }

    private Logger getLogger(String filename) {
        Logger logger = Logger.getLogger(MyWorker.class.getName());
        if (logger.getHandlers().length == 0) {
            try {
                FileHandler fileHandler = new FileHandler(getFilename(filename), true);
                fileHandler.setFormatter(new SimpleFormatter());
                // keep parent handler
                logger.addHandler(fileHandler);
                Log.i(TAG, "logger initialized");
            } catch (Exception ex) {
                Log.e(TAG, "can not get logger");
                logger = null;
            }
        }
        return logger;
    }

    private String getFilename(String path) {
        return path + LOG_FILENAME;
    }
}