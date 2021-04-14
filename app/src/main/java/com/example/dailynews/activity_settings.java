package com.example.dailynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class activity_settings extends AppCompatActivity {
    final Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SwitchCompat notificationToggle = findViewById(R.id.notificationToggle);
        notificationToggle.setTextOn("Включени");
        notificationToggle.setTextOff("Изгключени");
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
}