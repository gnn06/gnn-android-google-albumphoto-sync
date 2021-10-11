package gnn.com.googlealbumdownloadappnougat.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import gnn.com.googlealbumdownloadappnougat.R;

public class SettingsActivity extends AppCompatActivity implements IPresenterSettings {

    private IPresenterSettings presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        presenter = new PresenterSettings(this);
        PersistPrefSettings persistence = new PersistPrefSettings(this);
        persistence.restore(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PersistPrefSettings persistence = new PersistPrefSettings(this);
        persistence.save(presenter);
    }

    public long getCacheMaxAge() {
        long result = -1;
        TextView view = findViewById(R.id.editCacheMaxAge);
        if (!view.getText().equals("")) {
            result = hourToMs(Long.parseLong(view.getText().toString()));
        }
        return result;
    }

    @SuppressLint("SetTextI18n")
    public void setCacheMaxAge(long cacheMaxAge) {
        if (cacheMaxAge != -1) {
            TextView view = findViewById(R.id.editCacheMaxAge);
            view.setText(Long.toString(msToHour(cacheMaxAge)));
        }
    }

    long msToHour(long ms) {
        return ms / 1000 / 60 / 60;
    }

    long hourToMs(long hour) {
        return hour * 60 * 60 * 1000;
    }
}