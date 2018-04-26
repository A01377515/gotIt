package mx.itesm.wkt.gotita;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by Pablo on 21/02/18.
 */

public class AdapterRv extends RecyclerView.Adapter<AdapterRv.Vista> {

    private ArrayList<Offer> offers;
    private Context actualContext = null;
    String imgFeed;

    public AdapterRv(ArrayList<Offer> offers){
        this.offers=new ArrayList<Offer>(offers);
    }

    public AdapterRv(Context context, ArrayList<Offer> offers){
        this.offers=new ArrayList<Offer>(offers);
        actualContext = context;
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView tarjeta = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_libro, parent, false);

        return new Vista(tarjeta);
    }

    @Override
    public void onBindViewHolder(Vista holder, final int position) {

        final CardView tarjeta = holder.tarjeta;
        TextView titulo = tarjeta.findViewById(R.id.tvTitulo);
        TextView descripcion = tarjeta.findViewById(R.id.tvDescripcion);
        ImageView imagenProducto = tarjeta.findViewById(R.id.imageView);
        titulo.setText(offers.get(position).getTitle());
        descripcion.setText(offers.get(position).getDescription());


        int lenImages = offers.get(position).getImages().toArray().length;

        if (lenImages == 0){
            imgFeed = "https://firebasestorage.googleapis.com/v0/b/gotit-fa002.appspot.com/o/images%2FnoImgAvailable.png?alt=media&token=2e301719-b2b0-4bd8-8dce-aa3187e6e6c8";
        }else{
            imgFeed = offers.get(position).getImages().get(0);
        }

        Glide.with(actualContext)
                .load(imgFeed)
                .apply(RequestOptions.centerCropTransform())
                .into(imagenProducto);

        tarjeta.findViewById(R.id.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent productIntent = new Intent(v.getContext(), ProductActiv.class);
                productIntent.putExtra("Titulo",offers.get(position).getTitle());

                v.getContext().startActivity(productIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class Vista extends RecyclerView.ViewHolder {

        private CardView tarjeta;

        public Vista(CardView itemView) {
            super(itemView);
            this.tarjeta = itemView;
        }
    }
}
