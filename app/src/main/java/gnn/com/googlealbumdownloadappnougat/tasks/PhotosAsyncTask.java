package gnn.com.googlealbumdownloadappnougat.tasks;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterMain;

abstract class PhotosAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    static final String TAG = "goi";

    private boolean error = false;
    private String errorMessage;
    protected IPresenterMain presenter;


    PhotosAsyncTask(IPresenterMain presenter) {
        this.presenter = presenter;
    }

    void markAsError(String message) {
        this.error = true;
        this.errorMessage = message;
    }

    boolean isSuccessful() {
        return !this.error;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.error = false;
        this.errorMessage = null;
        presenter.setProgressBarVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        presenter.setProgressBarVisibility(ProgressBar.INVISIBLE);
        if (error) {
            presenter.showError(errorMessage);
        }
    }
}

