package com.fernando.beaconPem;

import android.os.Bundle;
import android.util.Log;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FIREBASE";
    RecyclerView recyclerView;
    AdaptadorCarta adaptadorCarta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Menu> options = new FirebaseRecyclerOptions.Builder<Menu>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Carta"),  new SnapshotParser< Menu >() {
                    @NonNull
                    @Override
                    public Menu parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Menu menu = snapshot.getValue(Menu.class);
                        menu.setNombre(snapshot.getKey());
                        return menu;
                    }
                }).build();

        adaptadorCarta = new AdaptadorCarta(options);
        recyclerView.setAdapter(adaptadorCarta);
    }

    @Override
    protected void onStart(){
        super.onStart();
        adaptadorCarta.startListening();
        Log.d(TAG, "Escuchando datos en la BBDD");
    }

    @Override
    protected void onStop(){
        super.onStop();
        adaptadorCarta.stopListening();
        Log.d(TAG, "Parar escuchar datos en la BBDD");
    }

}