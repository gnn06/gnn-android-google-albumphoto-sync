package gnn.com.photos;

import java.util.logging.Logger;

public class Log {
    private final Logger logger;

    public Log(String classname) {
        this.logger = Logger.getLogger(classname);
    }

    public void i(String tag, String message) {
        this.logger.info(message);
    }

    public void e(String tag, String message) {
        this.logger.severe(message);
    }
}
