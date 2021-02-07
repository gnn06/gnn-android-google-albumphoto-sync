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

    @Override
    protected void onPause() {
        super.onPause();
        TextView view = findViewById(R.id.editCacheMaxAge);
        long cacheMaxAge = Long.parseLong(view.getText().toString());
        Persistence persistence = new Persistence(this, null);
        persistence.saveSettings(cacheMaxAge);
    }
}