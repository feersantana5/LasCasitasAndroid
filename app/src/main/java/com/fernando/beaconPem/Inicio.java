package com.fernando.beaconPem;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class Inicio extends Activity implements BeaconConsumer {
    Button btn;
    protected static final String TAG = "BeaconAppIni";
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        btn = findViewById(R.id.idBotonCarta);
        verifyBluetooth();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siguiente = new Intent(Inicio.this, MainActivity.class);
                startActivity(siguiente);
                Log.d(TAG, "Boton a otra activity pulsado");
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Esta app necesita acceso a su ubicación en segundo plano."); //notificacion de notificacion en segundo plano
                            builder.setMessage("Por favor acepte la monotorización de su ubicación en segundo plano para poder encontrar su restaurante.");//notificacion de notificacion en segundo plano
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Cuidado"); //notificacion de notificacion en segundo plano
                            builder.setMessage("La ubicación en segundo plano no ha sido aceptada, la app no podrá encontrar su restaurante en segundo plano.  Por favor vaya a Ajustes -> Aplicaciones -> Permisos y otorge el acceso a a ubicación en segundo plano a esta aplicación.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Cuidado"); //notificacion de notificacion en segundo plano
                    builder.setMessage("La ubicación en segundo plano no ha sido aceptada, la app no podrá encontrar su restaurante en segundo plano. Por favor vaya a Ajustes -> Aplicaciones -> Permisos y otorge el acceso a a ubicación en segundo plano a esta aplicación.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }

            }
        }
    }
//Devuelve la decisión del usuario
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //fine location para GPS Y NETWORK, mas precisa
                    Log.d(TAG, "PERMISOS DE fine location AUTORIZADOS");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Cuidado"); //cuando no aceptamos la localizacion
                    builder.setMessage("Sin los permisos de localización, no podrá encontrar su restaurante.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }

                        });
                    }
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "PERMISOS DE UBICACIÓN EN SEGUNDO PLANO AUTORIZADOS");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Cuidado"); //notificacion de ubicacion en segundo plano
                    builder.setMessage("La ubicación en segundo plano no ha sido aceptada, la app no podrá encontrar su restaurante en segundo plano.  Por favor vaya a Ajustes -> Aplicaciones -> Permisos y otorge el acceso a a ubicación en segundo plano a esta aplicación.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }

                        });
                    }
                    builder.show();
                }
                return;
            }
        }
    }

    //Cuando la actividad entra en el estado Resumed, pasa al primer plano y, a continuación, el sistema
// invoca la devolución de llamada onResume(). Este es el estado en el que la app interactúa con el
// usuario. La app permanece en este estado hasta que ocurre algún evento que la quita de foco. Tal
// evento podría ser, por ejemplo, recibir una llamada telefónica, que el usuario navegue a otra
// actividad o que se apague la pantalla del dispositivo.
    @Override
    public void onResume() {
        super.onResume();
        BeaconReferenceApplication application = ((BeaconReferenceApplication) this.getApplicationContext());
        application.setInicio(this);
        beaconManager.bind(this);
        Log.d(TAG, "Enlazada Activity con Beacon Reference.");
    }

    //El sistema llama a este método a modo de primera indicación de que el usuario está abandonando tu
// actividad (aunque no siempre significa que está finalizando la actividad); esto indica que la
// actividad ya no está en primer plano (aunque puede seguir siendo visible si el usuario está en el
// modo multiventana). Utiliza el método onPause() para pausar o ajustar las operaciones que no deben
// continuar (o que deben continuar con moderación) mientras la activity se encuentra en estado Paused
// y que esperas reanudar en breve
    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication) this.getApplicationContext()).setInicio(null);
        beaconManager.unbind(this);
        Log.d(TAG, "Desenlazada Activity con Beacon Reference.");
    }

    /**
     * Comprueba que el bluetooth este activo y sea compatible, en caso que no envia notifica
     */
    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth desactivado"); //titulo notiffica si ble desactibvado
                builder.setMessage("Por favor active bluetooth y reinicie la aplicación."); //cuerpo notiffica si ble desactibvado
                builder.setPositiveButton(android.R.string.ok, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                }
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE no disponible"); //titulo notiffica si ble no compatible
            builder.setMessage("Disculpe, este dispositivo no soporta la tecnología Bluetooth LE."); //cuerpo notiffica si ble no compatible
            builder.setPositiveButton(android.R.string.ok, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }

                });
            }
            builder.show();
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "didRangeBeaconsInRegion called con:  "+beacons.size()+" beacons");
                    Beacon firstBeacon = beacons.iterator().next();
                    Log.d(TAG, "El restaurante con UUID:" + firstBeacon.getId1()+", Mayor:"+firstBeacon.getId2()+", Minor: " +firstBeacon.getId3() +" está a " + firstBeacon.getDistance() + " metros, con RSSI:"+ firstBeacon.getRssi()+".");
                }
            }

        };
        try {
            Beacon beacon = new Beacon.Builder().setId1("5A4BCFCE-174E-4BAC-A814-092E77F6B7E5").setId2("123").setId3("456").build();
            beaconManager.startRangingBeaconsInRegion(new Region("Casitas", beacon.getId1(), beacon.getId2(), beacon.getId3()));
            Log.d(TAG, "startRangingBeaconsInRegion called");
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

}