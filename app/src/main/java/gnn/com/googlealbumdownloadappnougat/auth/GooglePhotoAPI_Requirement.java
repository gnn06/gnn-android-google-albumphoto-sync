package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.view.IView;

public class GooglePhotoAPI_Requirement extends PermissionRequirement {

    public GooglePhotoAPI_Requirement(PermissionRequirement nextRequirement, AuthManager auth, IView view) {
        super(nextRequirement, auth, view);
    }

    @Override
    public boolean checkRequirement() {
        return auth.hasGooglePhotoPermission();
    }

    @Override
    public void askAsyncRequirement() {
        view.updateUI_User();
        auth.requestGooglePhotoPermission();
    }

    @Override
    public void exec() {

    }
}
