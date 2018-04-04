package mx.itesm.wkt.gotita;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by IRV1 on 21/02/18.
 */

public class AdapterRv extends RecyclerView.Adapter<AdapterRv.Vista> {


    private String[] arrTitulos;
    private String[] arrDescripciones;

    public AdapterRv(String[] titulos, String[] descripciones){
        arrTitulos = titulos;
        arrDescripciones = descripciones;
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
        titulo.setText(arrTitulos[position]);
        descripcion.setText(arrDescripciones[position]);
    }

    @Override
    public int getItemCount() {
        return arrTitulos.length;
    }

    public class Vista extends RecyclerView.ViewHolder {

        private CardView tarjeta;
        public Vista(CardView itemView) {
            super(itemView);
            this.tarjeta = itemView;
        }
    }
}
