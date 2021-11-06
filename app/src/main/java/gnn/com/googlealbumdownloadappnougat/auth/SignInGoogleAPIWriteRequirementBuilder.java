package gnn.com.googlealbumdownloadappnougat.auth;

import gnn.com.googlealbumdownloadappnougat.tasks.SyncTask;
import gnn.com.googlealbumdownloadappnougat.ui.view.IView;

public class SignInGoogleAPIWriteRequirementBuilder {

    static public Require build(final SyncTask task, AuthManager auth, IView view) {
        Exec exec = new Exec() {
            @Override
            public void exec() {
                task.execute();
            }
        };
        Require writeReq = new WritePermissionRequirement(exec, auth, view);
        Require signInReq = new SignInAndGoogleAuthRequirement(writeReq, auth, view);
        return signInReq;
    }

}
