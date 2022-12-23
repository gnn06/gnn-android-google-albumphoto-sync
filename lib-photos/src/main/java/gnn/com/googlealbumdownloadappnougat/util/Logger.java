package gnn.com.googlealbumdownloadappnougat.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {

    private static Logger _logger;
    static private String _filename;
    private static boolean _test;

    static public void configure(String filename) {
        _filename = filename  + "/gnnapp.log";
        _test = false;
    }

    // For test
    static public void configure() {
        _filename = null;
        _test = true;
    }

    static public Logger getLogger() {
        if (_logger == null) {
            if (_test == false && _filename == null) {
                throw new NullPointerException();
            }
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
        if (_test) {
            System.out.println(message);
        } else {
            if (_filename != null) {
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
        }
    }

    public void close()  {
    }
}
