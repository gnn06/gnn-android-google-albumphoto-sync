package com.gnn.photos;

import java.io.File;

import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerCLI extends Synchronizer {

    private final File credentialFolder;

    public SynchronizerCLI(File cacheFile, long cacheMaxAge, File processFolder, File credentialFolder) {
        super(cacheFile, cacheMaxAge, processFolder);
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
