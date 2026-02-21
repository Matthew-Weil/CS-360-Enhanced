package MainActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.weil_cs360_project_one.R;

public class SmsNotification extends AppCompatActivity {
    /**
     * Get permission from the user to send notifications
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted) {


                } else {


                }
            });

    /**
     * Method ran on creation
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_notifications_ui);


        requestNotificationPermission();

    }

    private void requestNotificationPermission() {
        // Check the permissions, if they are already permitted we dont need to ask again, else ask the user for permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                //Do nothing because the permissions are already good
                return;
            }
            else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    /**
     * If the inventoryBtn is clicked simply go back one activity
     * @param v
     */
    public void inventoryBtnClicked(View v) {
        finish();
    }

    /**
     * If the log out button is clicked skip back all the way to the login page
     * @param v
     */
    public void logOutBtnClicked(View v) {
        //Skip all the way to the first MainActivity and close everything in between (InventoryManagement)
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
