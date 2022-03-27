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

import org.apache.commons.lang3.ArrayUtils;

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
        ((SwitchCompat)getView().findViewById(R.id.SwitchWallPaper)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.onSwitchWallpaper(checked);
            }
        });
        getView().findViewById(R.id.SectionFreqeuncyWallpaper).setOnClickListener(v -> {
            presenter.chooseFrequencyWallpaper();
        });
        getView().findViewById(R.id.SectionFreqeuncySync).setOnClickListener(v -> {
            presenter.chooseFrequencySync();
        });
        getView().findViewById(R.id.SectionFreqeuncyUpdatePhotos).setOnClickListener(v -> {
            presenter.chooseFrequencyUpdate();
        });
        presenter.onAppStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onAppStop();
    }

    @Override
    public void setSwitchWallpaper(boolean scheduled) {
        SwitchCompat button = getView().findViewById(R.id.SwitchWallPaper);
        button.setChecked(scheduled);
    }

    @Override
    public void setFrequencyWallpaper(int frequency) {
        setTextFromFrequency(frequency, R.array.frequency_wallpaper_value, R.array.frequency_wallpaper_label, R.id.textFrequencyWallpaper);
    }

    @Override
    public void enableFrequencyWallpaper(boolean switchChecked) {
        View text = getView().findViewById(R.id.textFrequencyWallpaper);
        text.setEnabled(!switchChecked);
    }

    @Override
    public void setFrequencySync(int frequency) {
        setTextFromFrequency(frequency, R.array.frequency_sync_value, R.array.frequency_sync_label, R.id.textFrequencySync);
    }

    @Override
    public void enableFrequencySync(boolean switchChecked) {
        View text = getView().findViewById(R.id.textFrequencySync);
        text.setEnabled(!switchChecked);
    }

    @Override
    public void setFrequencyUpdate(int frequency) {
        setTextFromFrequency(frequency, R.array.frequency_update_value, R.array.frequency_update_label, R.id.textFrequencyUpdatePhotos);
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

    private void setTextFromFrequency(int frequency, int array_value, int array_label, int textView) {
        int[] codeArray = getResources().getIntArray(array_value);
        int index = ArrayUtils.indexOf(codeArray, frequency);
        String label = getResources().getStringArray(array_label)[index];

        TextView view = getView().findViewById(textView);
        view.setText(label);
    }

}
