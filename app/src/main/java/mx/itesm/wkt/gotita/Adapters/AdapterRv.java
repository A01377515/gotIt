package mx.itesm.wkt.gotita.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import mx.itesm.wkt.gotita.NavigationBar;
import mx.itesm.wkt.gotita.Offer;
import mx.itesm.wkt.gotita.ProductActiv;
import mx.itesm.wkt.gotita.R;

/**
 * Created by Pablo on 21/02/18.
 */

public class AdapterRv extends RecyclerView.Adapter<AdapterRv.Vista> {

    private ArrayList<Offer> offers;
    private Context actualContext = null;
    private String imgFeed;
    private NavigationBar nBar = NavigationBar.WALL;
    private Switch activeSwitch;

    public AdapterRv(ArrayList<Offer> offers){
        this.offers=new ArrayList<Offer>(offers);
    }

    public AdapterRv(Context context, ArrayList<Offer> offers, NavigationBar nBar){
        this.offers=new ArrayList<Offer>(offers);
        actualContext = context;
        this.nBar = nBar;
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView tarjeta;
        if (nBar == NavigationBar.WALL){
            tarjeta = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tarjeta_muro, parent, false);
        }else{
            tarjeta = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tarjeta_personal, parent, false);
        }



        return new Vista(tarjeta);
    }

    @Override
    public void onBindViewHolder(Vista holder, final int position) {

        final CardView tarjeta = holder.tarjeta;
        TextView titulo = tarjeta.findViewById(R.id.tvTitulo);
        TextView descripcion = tarjeta.findViewById(R.id.tvDescripcion);
        ImageView imagenProducto = tarjeta.findViewById(R.id.imageView);
        if(nBar == NavigationBar.PERSONAL){
            activeSwitch = tarjeta.findViewById(R.id.activeSwitch);

        }
        titulo.setText(offers.get(position).getTitle());
        String priceText = "$"+String.format("%,.2f",offers.get(position).getPrice().get("min"))+" - $"+String.format("%,.2f",offers.get(position).getPrice().get("max"));
        descripcion.setText(priceText);

        TextView schedule;
        if(nBar == NavigationBar.WALL){
            schedule = tarjeta.findViewById(R.id.scheduleStrWall);
        }else{
            schedule = tarjeta.findViewById(R.id.scheduleStrPersonal);
        }

        if (offers.get(position).getType().equals("Service")){
            schedule.setVisibility(View.VISIBLE);
            schedule.setText(offers.get(position).getSchedule().get("min")+" - "+offers.get(position).getSchedule().get("max"));
        }






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
        if (nBar == NavigationBar.WALL){
            if (offers.get(position).getType().equals("Service")){
                tarjeta.setCardBackgroundColor(Color.parseColor(tarjeta.getContext().getResources().getString(R.string.orange)));
            }else{
                tarjeta.setCardBackgroundColor(Color.parseColor(tarjeta.getContext().getResources().getString(R.string.blue)));
            }
        }


        tarjeta.findViewById(R.id.cardView).setOnClickListener(new View.OnClickListener() {
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
                if (offers.get(position).getType().equals("Service")){
                    productIntent.putExtra("scheduleMin",offers.get(position).getSchedule().get("min"));
                    productIntent.putExtra("scheduleMax",offers.get(position).getSchedule().get("max"));

                }






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
