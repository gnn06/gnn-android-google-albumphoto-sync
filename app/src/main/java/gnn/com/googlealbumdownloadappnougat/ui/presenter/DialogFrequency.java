package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import gnn.com.googlealbumdownloadappnougat.R;

class DialogFrequency {

    final private Context activity;
    final private IPresenterFrequencies presenter;

    DialogFrequency(IPresenterFrequencies presenter, Context activity) {
        this.presenter = presenter;
        this.activity = activity;
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.pick_frequency)
            .setItems(R.array.frequencies_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    Resources resources = activity.getResources();
                    int[] frequencyValue = resources.getIntArray(R.array.frequencies_value);
                    presenter.setFrequencyWallpaper(frequencyValue[which]);
                }
            });
        builder.create().show();
    }
}
