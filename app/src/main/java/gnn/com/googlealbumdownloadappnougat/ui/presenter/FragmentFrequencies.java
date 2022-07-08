package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.apache.commons.lang3.ArrayUtils;

import gnn.com.googlealbumdownloadappnougat.ServiceLocator;
import gnn.com.googlealbumdownloadappnougat.ui.UserModel;
import gnn.com.googlealbumdownloadappnougat.ui.view.IViewFrequencies;
import gnn.com.googlealbumdownloadappnougat.R;

public class FragmentFrequencies extends FragmentHighlight implements IViewFrequencies {

    private IPresenterFrequencies presenter;

    public FragmentFrequencies() {
        super(R.id.fragmentFrequencies);
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
        ServiceLocator.getInstance().setWallpaperSchedulerWithPermission(
            new WallpaperSchedulerWithPermission(getActivity(), getContext(),
                    ServiceLocator.getInstance().getWallpaperScheduler(),
                     this, new ViewModelProvider(getActivity()).get(UserModel.class)));
        this.presenter = new PresenterFrequencies( this, getContext());
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
    public void setFrequencyWallpaper(int frequency) {
        setTextFromFrequency(frequency, R.array.frequency_wallpaper_value, R.array.frequency_wallpaper_label, R.id.textFrequencyWallpaper);
    }

    @Override
    public void setFrequencySync(int frequency) {
        setTextFromFrequency(frequency, R.array.frequency_sync_value, R.array.frequency_sync_label, R.id.textFrequencySync);
    }

    @Override
    public void setFrequencyUpdate(int frequency) {
        setTextFromFrequency(frequency, R.array.frequency_update_value, R.array.frequency_update_label, R.id.textFrequencyUpdatePhotos);
    }

    @Override
    public void alertFrequencyError() {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.frequency_too_low))
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void alertNeedDisableSchedule() {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.frequency_need_disable_first))
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
        try {
            String label = null;
            label = getResources().getStringArray(array_label)[index];
            TextView view = getView().findViewById(textView);
            view.setText(label);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("GOI", "can not retrieve frequency'label for " + frequency + " in " + array_label);
        }
    }

}
