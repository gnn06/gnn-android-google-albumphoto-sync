package com.gnn.photos;

import java.io.File;

import gnn.com.photos.service.IScreenSizeAccessor;
import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerCLI extends Synchronizer {

    private final File credentialFolder;

    public SynchronizerCLI(File cacheFile, long cacheMaxAge, File processFolder, File credentialFolder, IScreenSizeAccessor screenSize) {
        super(cacheFile, cacheMaxAge, processFolder, null, screenSize);
        this.credentialFolder = credentialFolder;
    }

    @Override
    protected PhotosRemoteService getRemoteServiceImpl() {
        return new PhotosRemoteServiceCLI(credentialFolder);
    }

    @Override
    public void incAlbumSize() {

    }
}
