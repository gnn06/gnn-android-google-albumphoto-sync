package gnn.com.googlealbumdownloadappnougat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.IPresenterSettings;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.Persistence;
import gnn.com.googlealbumdownloadappnougat.ui.presenter.PresenterSettings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Persistence persistence = new Persistence(this);
        IPresenterSettings presenter = new PresenterSettings(this);
        persistence.restoreSettings(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Persistence persistence = new Persistence(this);
        persistence.saveSettings(this);
    }

    public long getCacheMaxAge() {
        long result = -1;
        TextView view = findViewById(R.id.editCacheMaxAge);
        if (!view.getText().equals("")) {
            result = Long.parseLong(view.getText().toString());
        }
        return result;
    }

    public void setCacheMaxAge(long cacheMaxAge) {
        if (cacheMaxAge != -1) {
            TextView view = findViewById(R.id.editCacheMaxAge);
            view.setText(Long.toString(cacheMaxAge));
        }
    }
}