package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class SignInGoogleAPIWriteRequirementBuilder {

    static public Require build(final SyncTask task, AuthManager auth, IView view, UserModel userModel) {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                task.execute();
            }
        };
        Require writeReq = new WritePermissionRequirement(exec, auth, view, userModel);
        Require googleAuth = new SignInWithAuthRequirement(writeReq, auth, view, userModel);
        Require signInReq = new SignInRequirement(googleAuth, auth, view, userModel);
        return signInReq;
    }

    static public Require build(Exec exec, AuthManager auth, IView view, UserModel userModel) {
        Require writeReq = new WritePermissionRequirement(exec, auth, view, userModel);
        Require googleAuth = new SignInWithAuthRequirement(writeReq, auth, view, userModel);
        Require signInReq = new SignInRequirement(googleAuth, auth, view, userModel);
        return signInReq;
    }
}
