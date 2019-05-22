package gnn.com.photos.sync;

import gnn.com.photos.model.Photo;

import java.util.ArrayList;

public class DiffCalculator {

    private ArrayList remote;
    private ArrayList local;
    private ArrayList toDownload;
    private ArrayList toDelete;

    /**
     * Determine le delta entre remote et local.
     * Returns :
     * - a list to download
     * - a list to delete
     * download new remote photo not in local, delete local not in remote.
     * how manage updated remote photo ?
     * photo.id is used
     * @param remote
     * @param  local
     * @return
     */
    public DiffCalculator(ArrayList remote, ArrayList local) {
        this.remote = remote;
        this.local = local;
        // TODO: 06/05/2019 determine delta
        this.toDownload = calculToDownload();
        this.toDelete   = calculToDelete();

        System.out.println("remote count = " + this.remote.size());
        System.out.println("local count = " + this.local.size());
        System.out.println("to download count = " + this.toDownload.size());
        System.out.println("to delete count = " + this.toDelete.size());
    }

   public ArrayList<Photo> calculToDownload() {
        ArrayList result = ((ArrayList)this.remote.clone());
        result.removeAll(this.local);
        return result;
    }

    private ArrayList calculToDelete() {
        ArrayList result = ((ArrayList)this.local.clone());
        result.removeAll(this.remote);
        return result;
    }

    public ArrayList getToDownload() {
        return toDownload;
    }

    public ArrayList getToDelete() {
        return toDelete;
    }
}
