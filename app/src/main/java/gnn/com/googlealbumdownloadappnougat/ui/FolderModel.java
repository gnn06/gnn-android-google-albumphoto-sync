package gnn.com.googlealbumdownloadappnougat.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FolderModel extends ViewModel {

    private final MutableLiveData<String> data = new MutableLiveData<>();

    public MutableLiveData<String> getFolder() {
        return data;
    }
}
