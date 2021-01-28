package com.fernando.beaconPem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fernando.beaconPem.R;

public class InfoMenu extends AppCompatActivity {
    private static final String TAG = "FIREBASE";
    Bundle datos;
    TextView nombreDetalle, precioDetalle, primerPlatoDetalle, segundoPlatoDetalle, postreDetalle;
    ImageView imagenDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_menu);

        datos = getIntent().getExtras();

        String nombreObtenido = datos.getString("nombre");
        nombreDetalle = (TextView) findViewById(R.id.idNombreDetalle);
        nombreDetalle.setText(nombreObtenido);

        Double precioObtenido = datos.getDouble("precio");
        precioDetalle = (TextView) findViewById(R.id.idPrecioDetalle);
        precioDetalle.setText(precioObtenido.toString()+" €");

        String primerPlatoObtenido = datos.getString("primerPlato");
        primerPlatoDetalle = (TextView) findViewById(R.id.idPrimerPlatoDetalle);
        primerPlatoDetalle.setText(primerPlatoObtenido);

        String segundoPlatoObtenido = datos.getString("segundoPlato");
        segundoPlatoDetalle = (TextView) findViewById(R.id.idSegundoPlatoDetalle);
        segundoPlatoDetalle.setText(segundoPlatoObtenido);

        String postreObtenido = datos.getString("postre");
        postreDetalle = (TextView) findViewById(R.id.idPostreDetalle);
        postreDetalle.setText(postreObtenido);

        String imagenURLObtenido = datos.getString("imagen");
        imagenDetalle = (ImageView) findViewById(R.id.idImagenDetalle);
        Glide.with(imagenDetalle.getContext()).load(imagenURLObtenido).into(imagenDetalle);

        Log.d(TAG, "Datos obtenidos y mostrados en el detalle.");

    }
}