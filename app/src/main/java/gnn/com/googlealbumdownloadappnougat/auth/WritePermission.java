package gnn.com.googlealbumdownloadappnougat.auth;

public class WritePermission extends PermissionRequirement {

    public WritePermission(PermissionRequirement nextRequirement, AuthManager auth) {
        super(nextRequirement, auth, null);
    }

    @Override
    public boolean checkRequirement() {
        return auth.hasWritePermission();
    }

    @Override
    public void askAsyncRequirement() {
        auth.requestWritePermission();
    }

    @Override
    public void exec() {
    }
}
