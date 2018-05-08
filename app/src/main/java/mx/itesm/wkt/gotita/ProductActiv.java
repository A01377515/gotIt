package mx.itesm.wkt.gotita;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import mx.itesm.wkt.gotita.Adapters.ViewPagerAdapter;

public class ProductActiv extends AppCompatActivity {

    private TextView titulo;
    private TextView desc;
    private TextView price;
    private ViewPager viewPager;
    private LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;
    private Button contact;
    // maps api
    private SupportMapFragment gMap;
    private static final float DEFAULT_ZOOM = 13f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        viewPager = findViewById(R.id.viewPagerDetalle);
        sliderDots = findViewById(R.id.sliderDots);
        titulo = findViewById(R.id.tituloStr);
        desc = findViewById(R.id.descStr);
        price = findViewById(R.id.priceStr);
        contact = findViewById(R.id.contactBtn);
        gMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);





        ArrayList<String> images = getIntent().getStringArrayListExtra("images");
        if(images.size() == 0){
            images.add("https://firebasestorage.googleapis.com/v0/b/gotit-fa002.appspot.com/o/images%2FnoImgAvailable.png?alt=media&token=2e301719-b2b0-4bd8-8dce-aa3187e6e6c8");
        }
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(this,images);
        viewPager.setAdapter(vpAdapter);

        dotsCount = vpAdapter.getCount();
        dots = new ImageView[dotsCount];

        loadDots();

        changeDotPosition();

        titulo.setText(getIntent().getStringExtra("Titulo").toUpperCase());
        desc.setText(getIntent().getStringExtra("desc"));
        price.setText("Precio: $"+getIntent().getStringExtra("priceMin")+" - $"+getIntent().getStringExtra("priceMax"));
        gMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng pos = new LatLng(getIntent().getDoubleExtra("lat",0),getIntent().getDoubleExtra("long",0));
                if (getIntent().getStringExtra("type").equals("Service")){
                    googleMap.addCircle(new CircleOptions()
                            .center(pos)
                            .radius(getIntent().getIntExtra("range",1000))
                            .strokeColor(Color.parseColor("#FFA500"))
                            .fillColor(Color.argb(100,255, 165, 0)));
                }else{
                    googleMap.addMarker(new MarkerOptions().position(pos));
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,DEFAULT_ZOOM));
            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailInt = new Intent(Intent.ACTION_SEND);
                emailInt.setData(Uri.parse("mailto:"));
                String[] emailadds = new String[1];
                emailadds[0] = getIntent().getStringExtra("email")+"";
                emailInt.putExtra(Intent.EXTRA_EMAIL,emailadds);
                emailInt.putExtra(Intent.EXTRA_SUBJECT,"Estoy interesado en "+getIntent().getStringExtra("Titulo"));
                emailInt.setType("text/plain");

                if (emailInt.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailInt, "Send Email"));
                }else{
                    Toast.makeText(getApplicationContext(),"No hay apps de correo instaladas",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void loadDots() {

        for (int i = 0; i < dotsCount; i++){
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0,8,0);
            sliderDots.addView(dots[i],params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));

    }

    private void changeDotPosition() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
