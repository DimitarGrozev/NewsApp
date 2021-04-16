package com.example.dailynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class activity_settings extends AppCompatActivity {
    final Database db = new Database(this);
    TextView txtNotifications, txtSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        txtNotifications = findViewById(R.id.txtNotifications);
        txtSettings = findViewById(R.id.txtSettings);
        SwitchCompat notificationToggle = findViewById(R.id.notificationToggle);
        SetUILanguage(getIntent().getStringExtra("country"));
        //db.CreateInitialStatus();
        Boolean notificationStatus = db.NotificationStatus();
        if(notificationStatus){
            notificationToggle.setChecked(true);
        }
        notificationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                db.ChangeNotificationStatus(isChecked);
            }
        });
    }

    private void SetUILanguage(String country){
        switch (country){
            case "gb":
                txtSettings.setText("SETTINGS");
                txtNotifications.setText("Notifications");
                break;
            case "de":
                txtSettings.setText("EINSTELLUNGEN");
                txtNotifications.setText("Benachrichtigungen");
                break;
            case "fr":
                txtSettings.setText("paramètres".toUpperCase());
                txtNotifications.setText("Notifications");
                break;
            case "ru":
                txtSettings.setText("НАСТРОЙКИ");
                txtNotifications.setText("Уведомления ");
                break;
            default:
                txtSettings.setText("НАСТРОЙКИ");
                txtNotifications.setText("Известия");
                break;
        }
    }
}