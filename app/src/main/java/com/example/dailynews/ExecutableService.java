package com.example.dailynews;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class ExecutableService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Random random = new Random();
        int number = random.nextInt(6);

        String message;
        String textContent;
        String name;
        String name3;
        String name1 = String.valueOf(MainActivity.selectedCountry);
        String name2 = String.valueOf(number);

        name = name1 + name2;
        int check = MainActivity.selectedCountry * 10 + number;

        switch (check) {
            case 0:
                message = "Новини на тема Бизнес";
                textContent = "Кликни за да отвориш приложението ни";
                break;
            case 1:
                message = "Новини на тема Спорт";
                textContent = "Кликни за да отвориш приложението ни";
                break;
            case 2:
                message = "Новини на тема Здраве";
                textContent = "Кликни за да отвориш приложението ни";
                break;
            case 3:
                message = "Новини на тема Технологии";
                textContent = "Кликни за да отвориш приложението ни";
                break;
            case 4:
                message = "Новини на тема Развлекателни";
                textContent = "Кликни за да отвориш приложението ни";
                break;
            case 5:
                message = "Новини на тема Наука";
                textContent = "Кликни за да отвориш приложението ни";
                break;
            case 10:
                message = "News in the world of Business";
                textContent = "Click on the notification to open the App";
                break;
            case 11:
                message = "Sports News";
                textContent = "Click on the notification to open the App";
                break;
            case 12:
                message = "News in the world of Health";
                textContent = "Click on the notification to open the App";
                break;
            case 13:
                message = "News in the world of Technology";
                textContent = "Click on the notification to open the App";
                break;
            case 14:
                message = "News in the world of Entertainment";
                textContent = "Click on the notification to open the App";
                break;
            case 15:
                message = "News in the world of Science";
                textContent = "Click on the notification to open the App";
                break;
            case 20:
                message = "Wirtschaftsnachrichten";
                textContent = "Klicken Sie hier, um unsere Anwendung zu öffnen";
                break;
            case 21:
                message = "Sport Nachrichten";
                textContent = "Klicken Sie hier, um unsere Anwendung zu öffnen";
                break;
            case 22:
                message = "Gesundheitsnachrichten";
                textContent = "Klicken Sie hier, um unsere Anwendung zu öffnen";
                break;
            case 23:
                message = "Technische Neuigkeiten";
                textContent = "Klicken Sie hier, um unsere Anwendung zu öffnen";
                break;
            case 24:
                message = "Unterhaltungsnachrichten";
                textContent = "Klicken Sie hier, um unsere Anwendung zu öffnen";
                break;
            case 25:
                message = "Wissenschaftsnachrichten";
                textContent = "Klicken Sie hier, um unsere Anwendung zu öffnen";
                break;
            case 30:
                message = "Actualité économique";
                textContent = "Cliquez ici pour ouvrir l'application";
                break;
            case 31:
                message = "Nouvelles sportives";
                textContent = "Cliquez ici pour ouvrir l'application";
                break;
            case 32:
                message = "Infos santé";
                textContent = "Cliquez ici pour ouvrir l'application";
                break;
            case 33:
                message = "Actualités technologiques";
                textContent = "Cliquez ici pour ouvrir l'application";
                break;
            case 34:
                message = "Actualités du divertissement";
                textContent = "Cliquez ici pour ouvrir l'application";
                break;
            case 35:
                message = "Actualités scientifiques";
                textContent = "Cliquez ici pour ouvrir l'application";
                break;
            case 40:
                message = "Actualité économique";
                textContent = "Нажмите здесь, чтобы открыть приложение";
                break;
            case 41:
                message = "Деловые новости";
                textContent = "Нажмите здесь, чтобы открыть приложение";
                break;
            case 42:
                message = "Новости здоровья";
                textContent = "Нажмите здесь, чтобы открыть приложение";
                break;
            case 43:
                message = "Новости технологий";
                textContent = "Нажмите здесь, чтобы открыть приложение";
                break;
            case 44:
                message = "Новости развлечений";
                textContent = "Нажмите здесь, чтобы открыть приложение";
                break;
            case 45:
                message = "Новости науки";
                textContent = "Нажмите здесь, чтобы открыть приложение";
                break;
            default:
                message = "Unknown language";
                textContent = "Error";
                break;
        }
        Intent intent1 = new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(name, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification")
                .setSmallIcon(R.drawable.outline_remove_circle_black_24dp)
                .setContentTitle(message)
                .setContentText(textContent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.logo))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.cancelAll();
        managerCompat.notify(1, builder.build());

    }
}
