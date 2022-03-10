package gnn.com.googlealbumdownloadappnougat.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserModel extends ViewModel {

    private final MutableLiveData<GoogleSignInAccount> userLiveData = new MutableLiveData<>();

    public MutableLiveData<GoogleSignInAccount> getUser() {
        return userLiveData;
    }

}
