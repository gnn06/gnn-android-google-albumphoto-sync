package gnn.com.googlealbumdownloadappnougat.ui;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FolderModel extends ViewModel {

    private final MutableLiveData<Uri> data = new MutableLiveData<>();

    public MutableLiveData<Uri> getFolder() {
        return data;
    }
}
