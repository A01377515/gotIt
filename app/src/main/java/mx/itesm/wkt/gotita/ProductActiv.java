package mx.itesm.wkt.gotita;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ProductActiv extends AppCompatActivity {

    private TextView titulo;
    private ImageView imagenProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        titulo = findViewById(R.id.tituloStr);
        imagenProducto = findViewById(R.id.imagenProducto);
        titulo.setText(getIntent().getStringExtra("Titulo"));


        String url = "https://firebasestorage.googleapis.com/v0/b/gotit-fa002.appspot.com/o/images%2Fpablo1.png?alt=media&token=2c90847e-7e89-459f-b51b-901f3cbf1c22";
        descargarImagen(url);

    }

    private void descargarImagen(String urlImg) {
        Glide.with(getApplicationContext()).load(urlImg)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Toast.makeText(getApplicationContext(),"Detener cargando imagen",Toast.LENGTH_SHORT).show();
                return false;
            }
        })
                .into(imagenProducto);
    }
}
