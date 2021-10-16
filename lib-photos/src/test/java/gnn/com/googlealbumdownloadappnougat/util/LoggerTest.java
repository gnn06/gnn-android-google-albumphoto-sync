package gnn.com.googlealbumdownloadappnougat.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoggerTest {

    @Test
    public void log() throws IOException {
        File folder = new File(System.getProperty("java.io.tmpdir"));
        System.out.println(folder.getAbsolutePath());

        Logger.configure(folder.getAbsolutePath());
        Logger logger = Logger.getLogger();

        logger.severe("severe");
        logger.info("info");
        logger.fine("fine");
        logger.finest("finest");

        logger = Logger.getLogger();

        logger.info("info222222");

        logger.close();

        logger = Logger.getLogger();
        logger.info("info appended");
        logger.close();
    }
}