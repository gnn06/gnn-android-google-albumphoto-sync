package gnn.com.googlealbumdownloadappnougat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import gnn.com.googlealbumdownloadappnougat.ui.presenter.Persistence;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public long getCacheMaxAge() {
        long result = -1;
        TextView view = findViewById(R.id.editCacheMaxAge);
        if (!view.getText().equals("")) {
            result = Long.parseLong(view.getText().toString());
        }
        return result;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Persistence persistence = new Persistence(this);
        persistence.saveSettings(this);
    }
}