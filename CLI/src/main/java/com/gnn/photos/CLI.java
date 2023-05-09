package com.gnn.photos;

import java.io.File;
import java.io.IOException;

import gnn.com.photos.LibContext;
import gnn.com.photos.service.IScreenSizeAccessor;
import gnn.com.photos.service.RemoteException;
import gnn.com.util.ScreenSize;

public class CLI {
    public static void main(String[] args) {
        System.out.println("googlephotos-sync start");
        try {
//            File folder = new File(System.getProperty("java.io.tmpdir"), "google-sync");
            File destinationFolder = new File(System.getProperty("user.home"), "googlephotos-sync");
            File processFolder = new File(destinationFolder, "data");
            File credentialFolder = new File(System.getProperty("user.home"), "credentials");

            IScreenSizeAccessor screenSizeAccessor = new IScreenSizeAccessor() {
                                      @Override
                                      public ScreenSize get() {
                                          return new ScreenSize(1024, 1024);
                                      }
                                  };
            SynchronizerCLI synchronizer = new SynchronizerCLI(processFolder, 24*7, processFolder, credentialFolder, screenSizeAccessor);
            synchronizer.syncRandom("Wallpaper", destinationFolder, "wallpaper", 10);

        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("googlephotos-sync end");
    }
}