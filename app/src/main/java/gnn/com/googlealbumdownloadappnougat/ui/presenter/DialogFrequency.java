package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import gnn.com.googlealbumdownloadappnougat.R;

class DialogFrequency {

    final private Context context;
    final private IPresenterFrequencies presenter;

    DialogFrequency(IPresenterFrequencies presenter) {
        this.presenter = presenter;
        this.context = presenter.getContext();
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.pick_frequency)
            .setItems(R.array.frequencies_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    Resources resources = context.getResources();
                    int[] frequencyValue = resources.getIntArray(R.array.frequencies_value);
                    presenter.setFrequencyWallpaper(frequencyValue[which]);
                }
            });
        builder.create().show();
    }
}
