package com.gnn.photos;

import java.io.File;

import gnn.com.photos.service.PhotoProvider;
import gnn.com.photos.service.PhotosRemoteService;

public class PhotosRemoteServiceCLI extends PhotosRemoteService {
    private final File credentialFolder;
    private PhotoProviderCLI _provider;

    public PhotosRemoteServiceCLI(File credentialFolder) {
        this.credentialFolder = credentialFolder;
    }

    @Override
    public PhotoProvider getPhotoProvider() {
        if (_provider == null) {
            // can not initialize Provider in constructor as activity is not set
            _provider = new PhotoProviderCLI(credentialFolder);
        }
        return _provider;
    }
}
