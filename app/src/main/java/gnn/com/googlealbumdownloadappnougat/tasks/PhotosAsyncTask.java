package gnn.com.googlealbumdownloadappnougat.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import gnn.com.googlealbumdownloadappnougat.ApplicationContext;
import gnn.com.googlealbumdownloadappnougat.auth.PersistOauthError;
import gnn.com.googlealbumdownloadappnougat.service.ISyncOauth;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterHome;

abstract class PhotosAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements ISyncOauth<Result> {

    static final String TAG = "goi";

    private final Context context;
    private boolean error = false;
    private String errorMessage;
    protected IPresenterHome presenter;
    private final PersistOauthError persistOauthError;


    PhotosAsyncTask(IPresenterHome presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        persistOauthError = new PersistOauthError(ApplicationContext.getInstance(context).getProcessFolder());
    }

    // For test
    PhotosAsyncTask(IPresenterHome presenter, Context context, PersistOauthError persistOauth) {
        this.presenter = presenter;
        this.context = context;
        persistOauthError = persistOauth;
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

    @Override
    public PersistOauthError getPersistOAuth() {
        return persistOauthError;
    }
}

