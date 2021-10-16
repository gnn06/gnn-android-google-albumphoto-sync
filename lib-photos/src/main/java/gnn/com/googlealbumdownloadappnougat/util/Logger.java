package gnn.com.googlealbumdownloadappnougat.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {

    private static Logger logger;

    static public Logger getLogger(String filename) {
        if (logger == null) {
            logger = new Logger(filename);
        }
        return logger;
    }

    private FileWriter fw;

    public Logger(String filename) {
        try {
            fw = new FileWriter(filename, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info(String message)  {
        message(message);
    }

    public void severe(String message)  {
        message(message);
    }

    public void finest(String message)  {
        message(message);
    }

    public void close()  {
        try {
            fw.close();
            logger = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fine(String message) {
        message(message);
    }

    private void message(String message) {
        try {
            Date date = new Date();
            fw.write(date.toString() + " " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
