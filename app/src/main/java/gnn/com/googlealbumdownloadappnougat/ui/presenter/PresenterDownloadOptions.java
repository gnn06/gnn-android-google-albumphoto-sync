package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.ui.view.IViewDoanloadOptions;

public class PresenterDownloadOptions implements IPresenterDownloadOptions {

    final private IViewDoanloadOptions view;

    public PresenterDownloadOptions(IViewDoanloadOptions view) {
        this.view = view;
    }

    // --- quantity ---
    /**
     * @return -1 if no quantity specified
     */
    @Override
    public int getQuantity() {
        String quantity = view.getQuantity();
        // replace "" into "-1"
        return Integer.parseInt(quantity.equals("") ? "-1" : quantity);
    }

    @Override
    public void setQuantity(int quantity) {
        // replace -1 into ""
        view.setQuantity(quantity == -1 ? "" : Integer.toString(quantity));
    }

    @Override
    public String getRename() {
        return view.getRename();
    }

    public void setRename(String rename) {
        view.setRename(rename);
    }

}
