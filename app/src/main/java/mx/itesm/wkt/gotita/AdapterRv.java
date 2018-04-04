package mx.itesm.wkt.gotita;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by IRV1 on 21/02/18.
 */

public class AdapterRv extends RecyclerView.Adapter<AdapterRv.Vista> {

    private ArrayList<Offer> offers;

    public AdapterRv(ArrayList<Offer> offers){
        this.offers=new ArrayList<Offer>(offers);
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView tarjeta = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_libro, parent, false);

        return new Vista(tarjeta);
    }

    @Override
    public void onBindViewHolder(Vista holder, int position) {

        CardView tarjeta = holder.tarjeta;
        TextView titulo = tarjeta.findViewById(R.id.tvTitulo);
        TextView descripcion = tarjeta.findViewById(R.id.tvDescripcion);
        titulo.setText(offers.get(position).getTitle());
        descripcion.setText(offers.get(position).getDescription());
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
