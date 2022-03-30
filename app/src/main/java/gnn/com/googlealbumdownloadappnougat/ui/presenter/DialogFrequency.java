package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import gnn.com.googlealbumdownloadappnougat.R;

/**
 * Choose a frequency
 * @return call the callback as presenter.setFrequencexxx(frequency value from array)
 *
 */
class DialogFrequency {

    final private Context context;
    private final IFrequencyPresenterSetter presenterCallback;
    private final int frequency_label;
    private final int frequency_value;

    DialogFrequency(Context context, IFrequencyPresenterSetter presenterCallback, int frequency_value, int frequency_label) {
        this.context = context;
        this.presenterCallback = presenterCallback;
        this.frequency_label = frequency_label;
        this.frequency_value = frequency_value;
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.pick_frequency)
            .setItems(frequency_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    Resources resources = context.getResources();
                    int[] frequencyValue = resources.getIntArray(frequency_value);
                    presenterCallback.setFrequency(frequencyValue[which]);
                }
            }).setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }


}
