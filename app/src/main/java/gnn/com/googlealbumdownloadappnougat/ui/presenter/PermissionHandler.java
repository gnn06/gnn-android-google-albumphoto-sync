package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import gnn.com.googlealbumdownloadappnougat.auth.Require;

public class PermissionHandler {

    private Require pendingRequirement;

    void startRequirement(Require require) {
        this.pendingRequirement = require;
        require.exec();
    }

    public void handlePermission(int result) {
        if (pendingRequirement != null) {
            Require nextRequirement = pendingRequirement.getNextRequire();
            pendingRequirement.resumeRequirement(result);
            if (nextRequirement != null) {
                pendingRequirement = nextRequirement;
            }
        }
    }

    Require getPendingRequirement() {
        return this.pendingRequirement;
    }
}
