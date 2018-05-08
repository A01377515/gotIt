package mx.itesm.wkt.gotita;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import io.apptik.widget.MultiSlider;

public class AgregarProdActiv extends AppCompatActivity {

    private MultiSlider maxSlider;
    private MultiSlider minSlider;
    private TextView maxTxt;
    private TextView minTxt;
    private TextView titulo;
    private TextView desc;
    private FloatingActionButton fab;
    private FirebaseFirestore db;
    private FirebaseUser currUser;
    private FirebaseStorage fStorage;
    private StorageReference sReference;
    private TextView txtChoose;
    private ImageView imgProd;
    private Uri imgPath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_prod);

        maxSlider = findViewById(R.id.prodMaxSlider);
        minSlider = findViewById(R.id.prodMinSlider);
        maxTxt = findViewById(R.id.prodMaxRange);
        minTxt = findViewById(R.id.prodMinRange);
        maxSlider.setMin(100);
        maxSlider.setMax(10000);
        minSlider.setMin(100);
        minSlider.setMax(10000);
        fab = findViewById(R.id.floatingActionButtonProd);
        titulo = findViewById(R.id.etTituloProd);
        desc = findViewById(R.id.etDescripcionProd);
        txtChoose = findViewById(R.id.txtAddImgProd);
        imgProd = findViewById(R.id.imgProd);

        titulo = findViewById(R.id.etTituloProd);
        desc = findViewById(R.id.etDescripcionProd);

        db = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        sReference = fStorage.getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();


        setDefaultValues();

        setSlideListeners();

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(titulo.getText().toString().trim().equals("")){
                    titulo.setError("Campo requerido");
                    return;
                }

                if (imgPath != null){
                    final ProgressDialog pDialog = new ProgressDialog(v.getContext());
                    pDialog.setTitle("Subiendo fotos");
                    pDialog.show();

                    StorageReference ref = sReference.child("images/*"+ UUID.randomUUID().toString());
                    ref.putFile(imgPath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pDialog.setMessage((int)progress+"%");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pDialog.dismiss();
                            addRegister(taskSnapshot.getDownloadUrl().toString());

                        }
                    });

                }else{
                    addRegister("https://firebasestorage.googleapis.com/v0/b/gotit-fa002.appspot.com/o/images%2FnoImgAvailable.png?alt=media&token=2e301719-b2b0-4bd8-8dce-aa3187e6e6c8");
                }
            }
        });

        txtChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



    }

    private void addRegister(String url) {

        ArrayList<String> images = new ArrayList<String>();
        HashMap<String,Double> price = new HashMap<String, Double>();
        GeoPoint location = new GeoPoint(19.596252,-99.227176);
        HashMap<String,String> schedule = new HashMap<String, String>();
        price.put("min",Double.parseDouble(minTxt.getText().toString()));
        price.put("max",Double.parseDouble(maxTxt.getText().toString()));
        images.add(url);
        Offer of = new Offer(true,desc.getText()+"",images,price,titulo.getText()+"","Product",currUser.getUid(),location,1000,schedule,currUser.getEmail());

        db.collection("feed").add(of).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(AgregarProdActiv.this,AgregarProdActiv.this.getResources().getString(R.string.firebaseSuccess),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarProdActiv.this,AgregarProdActiv.this.getResources().getString(R.string.firebaseFailure),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgPath = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),imgPath);
                imgProd.setVisibility(View.VISIBLE);
                imgProd.setImageBitmap(bm);
                imgProd.setAdjustViewBounds(true);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent imgInt = new Intent();
        imgInt.setType("image/*");
        imgInt.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imgInt,"Seleccionar imagen"),PICK_IMAGE_REQUEST);
    }



    private void setSlideListeners() {
        maxSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                maxTxt.setText(value+"");
            }
        });

        minSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                minTxt.setText(value+"");
            }
        });

    }

    private void setDefaultValues() {
        maxTxt.setText(maxSlider.getMin()+"");
        minTxt.setText(minSlider.getMin()+"");
    }
}
