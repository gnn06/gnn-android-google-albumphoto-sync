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

    private Logger(String filename) {
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
        FileWriter fw = null;
        try {
            fw = new FileWriter(_filename, true);
            Date date = new Date();
            fw.write(date.toString() + " " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close()  {
    }
}
