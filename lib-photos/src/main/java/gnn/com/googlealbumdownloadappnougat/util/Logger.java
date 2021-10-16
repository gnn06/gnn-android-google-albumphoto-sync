package gnn.com.googlealbumdownloadappnougat.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {

    private static Logger _logger;
    static private String _filename;

    static public void configure(String filename) {
        _filename = filename  + "/gnnapp.log";
    }

    static public Logger getLogger() {
        if (_logger == null) {
            _logger = new Logger(_filename);
        }
        return _logger;
    }

    private FileWriter fw;

    private Logger(String filename) {
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

    public void fine(String message) {
        message(message);
    }

    public void finest(String message)  {
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

    public void close()  {
        try {
            fw.close();
            _logger = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
