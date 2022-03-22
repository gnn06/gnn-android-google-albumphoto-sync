package gnn.com.googlealbumdownloadappnougat.ui.presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import gnn.com.googlealbumdownloadappnougat.R;

class DialogFrequency {

    final private Context activity;

    DialogFrequency(Context activity) {
        this.activity = activity;
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.pick_frequency)
            .setItems(R.array.frequencies, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                }
            });
        builder.create().show();
    }
}
