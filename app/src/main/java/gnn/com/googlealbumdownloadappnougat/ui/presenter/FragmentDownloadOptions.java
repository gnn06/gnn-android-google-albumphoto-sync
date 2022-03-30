package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gnn.com.googlealbumdownloadappnougat.R;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterDownloadOptions;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PersistPrefMain;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterDownloadOptions;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewDoanloadOptions;

public class FragmentDownloadOptions extends Fragment implements IViewDoanloadOptions {

    private IPresenterDownloadOptions presenter;

    public FragmentDownloadOptions() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_downloadoptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new PresenterDownloadOptions(this);
        new PersistPrefMain(getContext()).restoreDownloadOptions(presenter);
    }

    @Override
    public void onPause() {
        super.onPause();
        PersistPrefMain preferences = new PersistPrefMain(getContext());
        preferences.saveDownloadOption(presenter);
    }

    @Override
    /*
      @return String "" if empty
     */
    public String getQuantity() {
        TextView view = getView().findViewById(R.id.textQuantity);
        return view.getText().toString();
    }

    @Override
    /*
      @param quantity "" if no quantity
     */
    public void setQuantity(String quantity) {
        TextView view = getView().findViewById(R.id.textQuantity);
        view.setText(quantity);
    }

    @Override
    public String getRename() {
        // getText returns "" if empty
        // transform "" into null
        String value = ((TextView) getView().findViewById(R.id.textRename)).getText().toString();
        return "".equals(value) ? null : value;
    }

    @Override
    public void setRename(String rename) {
        // transform null into ""
        TextView view = getView().findViewById(R.id.textRename);
        view.setText(rename == null ? "" : rename);
    }

}