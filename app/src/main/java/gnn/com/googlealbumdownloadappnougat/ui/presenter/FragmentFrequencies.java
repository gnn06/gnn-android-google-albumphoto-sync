package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.MainActivity;
import gnn.com.googlealbumdownloadappnougat.R;

public class FragmentFrequencies extends Fragment implements IViewFrequencies {

    private IPresenterFrequencies presenter;

    public FragmentFrequencies() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frequencies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.presenter = new PresenterFrequencies( this, getContext(), (MainActivity) getActivity());
        new PersistPrefMain(getContext()).restoreFrequencies(presenter);
        ((SwitchCompat)getView().findViewById(R.id.SwitchWallPaper)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.onSwitchWallpaper(checked);
            }
        });
        getView().findViewById(R.id.textExplenationFrequencyWallpaper).setOnClickListener(v -> {
            presenter.chooseFrequency();
        });
        presenter.onAppStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        new PersistPrefMain(getContext()).saveFrequencies(this.presenter);
    }

    @Override
    public void setSwitchWallpaper(boolean scheduled) {
        SwitchCompat button = getView().findViewById(R.id.SwitchWallPaper);
        button.setChecked(scheduled);
    }

    @Override
    public String getFrequencyWallpaper() {
        TextView view = getView().findViewById(R.id.textFrequencyWallpaper);
        return view.getText().toString();
    }

    @Override
    public void setFrequencyWallpaper(String frequency) {
        TextView view = getView().findViewById(R.id.textFrequencyWallpaper);
        view.setText(frequency);
    }

    @Override
    public void enableFrequencyWallpaper(boolean switchChecked) {
        View text = getView().findViewById(R.id.textFrequencyWallpaper);
        text.setEnabled(!switchChecked);
    }

    @Override
    public String getFrequencySync() {
        TextView view = getView().findViewById(R.id.textFrequencySync);
        return view.getText().toString();
    }

    @Override
    public void setFrequencySync(String frequency) {
        TextView view = getView().findViewById(R.id.textFrequencySync);
        view.setText(frequency);
    }

    @Override
    public void enableFrequencySync(boolean switchChecked) {
        View text = getView().findViewById(R.id.textFrequencySync);
        text.setEnabled(!switchChecked);
    }

    @Override
    public String getFrequencyUpdatePhotos() {
        TextView view = getView().findViewById(R.id.textFrequencyUpdatePhotos);
        return view.getText().toString();
    }

    @Override
    public void setFrequencyUpdatePhotos(String frequency) {
        TextView view = getView().findViewById(R.id.textFrequencyUpdatePhotos);
        view.setText(frequency);
    }

    @Override
    public void enableFrequencyUpdatePhotos(boolean switchChecked) {
        View text = getView().findViewById(R.id.textFrequencyUpdatePhotos);
        text.setEnabled(!switchChecked);
    }

    @Override
    public void alertFrequencyError() {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.frequency_too_low))
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void showError(String message) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.error))
                    .setMessage(message)
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
    }
}
