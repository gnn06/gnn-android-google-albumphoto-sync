package gnn.com.photos;

import gnn.com.photos.service.IScreenSizeAccessor;

public class LibContext {

    static private IScreenSizeAccessor _ScreenSizeAccessor;

    static public void initialize(IScreenSizeAccessor screenSizeAccessor) {
        _ScreenSizeAccessor = screenSizeAccessor;
    }

    static public IScreenSizeAccessor getScreenSizeAccessor() {
        return _ScreenSizeAccessor;
    }
}