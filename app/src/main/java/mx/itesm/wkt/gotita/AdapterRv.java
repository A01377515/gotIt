package mx.itesm.wkt.gotita;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
                .inflate(R.layout.tarjeta_personal, parent, false);


        return new Vista(tarjeta);
    }

    @Override
    public void onBindViewHolder(Vista holder, final int position) {

        final CardView tarjeta = holder.tarjeta;
        TextView titulo = tarjeta.findViewById(R.id.tvTitulo);
        TextView descripcion = tarjeta.findViewById(R.id.tvDescripcion);
        ImageView imagenProducto = tarjeta.findViewById(R.id.imageView);
        titulo.setText(offers.get(position).getTitle());
        descripcion.setText("$"+String.format("%,.2f",offers.get(position).getPrice().get("min"))+" - $"+String.format("%,.2f",offers.get(position).getPrice().get("max")));


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

        if (offers.get(position).getType().equals("Service")){
            tarjeta.setCardBackgroundColor(Color.parseColor("#FFA500"));
        }else{
            tarjeta.setCardBackgroundColor(Color.parseColor("#00c5ff"));
        }

        tarjeta.findViewById(R.id.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent productIntent = new Intent(v.getContext(), ProductActiv.class);
                productIntent.putStringArrayListExtra("images",offers.get(position).getImages());
                productIntent.putExtra("Titulo",offers.get(position).getTitle());
                productIntent.putExtra("desc",offers.get(position).getDescription());
                productIntent.putExtra("priceMin",String.format("%,.2f",offers.get(position).getPrice().get("min"))+"");
                productIntent.putExtra("priceMax",String.format("%,.2f",offers.get(position).getPrice().get("max"))+"");
                productIntent.putExtra("lat",offers.get(position).getLocation().getLatitude());
                productIntent.putExtra("long",offers.get(position).getLocation().getLongitude());
                productIntent.putExtra("range",offers.get(position).getRange());
                productIntent.putExtra("type",offers.get(position).getType());






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
