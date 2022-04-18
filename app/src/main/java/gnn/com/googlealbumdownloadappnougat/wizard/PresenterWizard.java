package gnn.com.googlealbumdownloadappnougat.wizard;

import android.app.Activity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import gnn.com.googlealbumdownloadappnougat.R;

public class PresenterWizard {

    final private Activity activity;

    public PresenterWizard(Activity activity) {
        this.activity = activity;
    }

    public void switchToApp() {
        NavController controller = Navigation.findNavController(this.activity, R.id.fragment_container_view);
        controller.navigate(R.id.action_fragmentWizard_to_fragmentHome);
    }
}
