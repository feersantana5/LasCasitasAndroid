package com.fernando.beaconPem;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;


public class BeaconReferenceApplication extends Application implements BootstrapNotifier {
    private static final String TAG = "FERBeaconApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private Inicio inicio = null;

    public void onCreate() {
        super.onCreate();
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setDebug(true);

        Log.d(TAG, "AJUSTANDO MONOTORIZACION EN SEGUNDO PLANO PARA BEACON Y MODO AHORRO");
        // funciona cuando se descubre un beacon
        // creamos el nuestro
        Beacon beacon = new Beacon.Builder().setId1("5A4BCFCE-174E-4BAC-A814-092E77F6B7E5").setId2("123").setId3("456").build();
        Region region = new Region("Casitas", beacon.getId1(), beacon.getId2(), beacon.getId3());

        regionBootstrap = new RegionBootstrap(this, region);
        // reduce la potencia del bluetooth 60% cuando esta en background
        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "did enter region called.");
        //envía notificación al usuario cuando se encuentra un Beacon dentro de la region definida previamente
        Log.d(TAG, "ENVIANDO NOTIFICACIÓN:");
        sendNotification();
        if (inicio != null) {
            //presentamos si el beacon ya es conocido
            Log.d(TAG,"BEACON CONOCIDA DETECTADA DE NUEVO" );
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "did exit region called.");
        Log.d(TAG,"YA NO HAY BEACON CERCA SUYO." );
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG,"EL ESTADO ACTUAL DE LA REGION ES: " + (state == 1 ? "DENTRO" : "FUERA ("+state+")") );
    }

    /**
     * creamos la notificacion que mostramos al usuario cuando encuentra el beacon
     */
    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Beacon Reference Notifications",
                    "Beacon Reference Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(this, channel.getId());
        }
        else {
            builder = new Notification.Builder(this);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, Inicio.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.carta);
        builder.setContentTitle("Está cerca de Las Casitas");  //titulo de notif cuando detecta un beacon
        builder.setContentText("Presione aqui para ver el menu de su restaurante!"); //cuerpo de notif cuando detecta un beacon
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(1, builder.build());
    }

    public void setInicio(Inicio activity) {
        this.inicio = activity;
    }

}
