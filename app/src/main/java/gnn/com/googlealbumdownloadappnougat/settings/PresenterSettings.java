package gnn.com.googlealbumdownloadappnougat.settings;

public class PresenterSettings implements IPresenterSettings {

    private final SettingsActivity view;

    public PresenterSettings(SettingsActivity view) {
        this.view = view;
    }

    public static final long DefaultCacheMaxAge = 24 * 60 * 60 * 1000;

}
