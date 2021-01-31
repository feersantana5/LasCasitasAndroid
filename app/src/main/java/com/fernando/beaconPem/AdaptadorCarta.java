package com.fernando.beaconPem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdaptadorCarta  extends FirebaseRecyclerAdapter<Menu,AdaptadorCarta.myViewHolder>{
    private static final String TAG = "BeaconAppAdap";
    Context context;

    public AdaptadorCarta(@NonNull FirebaseRecyclerOptions<Menu> options) {
        super(options);
    }

    //mostrar la información del menu en el recyclerview
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull final Menu menu) {
        myViewHolder.nombre.setText(menu.getNombre());
        myViewHolder.precio.setText(menu.getPrecio().toString()+" €");
        myViewHolder.primerPlato.setText(menu.getPrimerPlato());
        myViewHolder.segundoPlato.setText(menu.getSegundoPlato());
        myViewHolder.postre.setText(menu.getPostre());
        Glide.with(myViewHolder.imagen.getContext()).load(menu.getImagen()).into(myViewHolder.imagen);

        Log.d(TAG, "Obtenidos datos de Firebase.");

        //pasar la información del menu al detalle
        context = myViewHolder.itemView.getContext();
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, menu.getNombre(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, InfoMenu.class);
                intent.putExtra("nombre", menu.getNombre());
                intent.putExtra("precio", menu.getPrecio());
                intent.putExtra("primerPlato", menu.getPrimerPlato());
                intent.putExtra("segundoPlato", menu.getSegundoPlato());
                intent.putExtra("postre", menu.getPostre());
                intent.putExtra("imagen", menu.getImagen());

            context.startActivity(intent);
                Log.d(TAG, "Datos enviados al detalle.");
            }
        });
    }

    @NonNull
    @Override
    //crea el item
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen;
        TextView nombre, precio, primerPlato, segundoPlato, postre;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = (ImageView) itemView.findViewById(R.id.idImagen);
            nombre = (TextView) itemView.findViewById(R.id.idNombre);
            precio = (TextView) itemView.findViewById(R.id.idPrecio);
            primerPlato = (TextView) itemView.findViewById(R.id.idPrimerPlato);
            segundoPlato = (TextView) itemView.findViewById(R.id.idSegundoPlato);
            postre = (TextView) itemView.findViewById(R.id.idPostre);
        }
    }

}
