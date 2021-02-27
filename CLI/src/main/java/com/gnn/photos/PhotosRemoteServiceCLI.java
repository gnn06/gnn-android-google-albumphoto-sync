package com.gnn.photos;

import gnn.com.photos.service.PhotoProvider;
import gnn.com.photos.service.PhotosRemoteService;

public class PhotosRemoteServiceCLI extends PhotosRemoteService {
    private PhotoProviderCLI _provider;

    @Override
    public PhotoProvider getPhotoProvider() {
        if (_provider == null) {
            // can not initialize Provider in constructor as activity is not set
            _provider = new PhotoProviderCLI();
        }
        return _provider;
    }
}
