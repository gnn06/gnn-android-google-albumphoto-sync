package gnn.com.googlealbumdownloadappnougat;

import android.content.Context;

import java.io.File;

public class ApplicationContext {

    private static ApplicationContext instance;

    private ApplicationContext(String cachePath, String processPath) {
        this.cachePath = cachePath;
        this.processPath = processPath;
    }

    public static ApplicationContext getInstance(Context context) {
        if (instance == null) {
            String cachePath = context.getFilesDir().getAbsolutePath() + "/cache";
            String processPath = context.getFilesDir().getAbsolutePath();
            instance = new ApplicationContext(cachePath, processPath);
        }
        return instance;
    }

    private final String cachePath;
    private final String processPath;

    public String getCachePath() {
        return cachePath;
    }

    public String getProcessPath() {
        return processPath;
    }

    public File getProcessFolder() {
        return new File(getProcessPath());
    }
}
