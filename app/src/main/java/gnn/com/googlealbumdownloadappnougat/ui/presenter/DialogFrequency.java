package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import gnn.com.googlealbumdownloadappnougat.R;

class DialogFrequency {

    final private Context context;
    private final IFrequencyPresenterSetter presenterCallback;

    DialogFrequency(Context context, IFrequencyPresenterSetter presenterCallback) {
        this.context = context;
        this.presenterCallback = presenterCallback;
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.pick_frequency)
            .setItems(R.array.frequency_wallpaper_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    Resources resources = context.getResources();
                    int[] frequencyValue = resources.getIntArray(R.array.frequency_wallpaper_value);
                    presenterCallback.setFrequency(frequencyValue[which]);
                }
            });
        builder.create().show();
    }


}
