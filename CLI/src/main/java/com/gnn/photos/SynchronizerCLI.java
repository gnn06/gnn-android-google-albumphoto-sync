package com.gnn.photos;

import java.io.File;

import gnn.com.photos.service.PhotosRemoteService;
import gnn.com.photos.sync.Synchronizer;

public class SynchronizerCLI extends Synchronizer {

    public SynchronizerCLI(File cacheFile, long cacheMaxAge, File processFolder) {
        super(cacheFile, cacheMaxAge, processFolder);
    }

    @Override
    protected PhotosRemoteService getRemoteServiceImpl() {
        return new PhotosRemoteServiceCLI();
    }

    @Override
    public void incAlbumSize() {

    }
}
