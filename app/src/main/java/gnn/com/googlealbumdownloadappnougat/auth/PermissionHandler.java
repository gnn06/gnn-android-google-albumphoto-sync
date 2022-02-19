package gnn.com.googlealbumdownloadappnougat.auth;

public class PermissionHandler {

    private Require pendingRequirement;

    public void startRequirement(Require require) {
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

    public Require getPendingRequirement() {
        return this.pendingRequirement;
    }
}
