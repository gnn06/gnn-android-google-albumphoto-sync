package gnn.com.googlealbumdownloadappnougat.settings;

public class PresenterSettings implements IPresenterSettings {

    private final SettingsActivity view;

    public PresenterSettings(SettingsActivity view) {
        this.view = view;
    }

    @Override
    public long getCacheMaxAge() {
        return view.getCacheMaxAge();
    }

    @Override
    public void setCacheMaxAge(long cacheMaxAge) {
        view.setCacheMaxAge(cacheMaxAge);
    }

    public static final long DefaultCacheMaxAge = 24 * 60 * 60 * 1000;

}
