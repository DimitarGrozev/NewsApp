package com.example.dailynews;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHandler {
    private Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }

    public void setAlarmManager() {
        Intent intent = new Intent(context, ExecutableService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager noti = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (noti != null) {
            long TriggerAfter = 10000;
            long TriggerEvery = 10000;
            noti.setRepeating(AlarmManager.RTC_WAKEUP, TriggerAfter, TriggerEvery, sender);
        }
    }

    public void cancelAlarmManager() {
        Intent intent = new Intent(context, ExecutableService.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager noti = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        noti.cancel(sender);

    }
}
