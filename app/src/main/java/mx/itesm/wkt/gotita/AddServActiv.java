package mx.itesm.wkt.gotita;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class AddServActiv extends AppCompatActivity {

    private MultiSlider maxSlider;
    private MultiSlider minSlider;
    private TextView maxTxt;
    private TextView minTxt;
    private TextView minHrs;
    private TextView maxHrs;
    private TextView titulo;
    private TextView desc;
    private String maxHrsStr;
    private String minHrsStr;
    private FloatingActionButton fab;
    private FirebaseFirestore db;
    private FirebaseStorage fStorage;
    private StorageReference sReference;
    private FirebaseUser currUser;
    private TextView txtChoose;
    private ImageView imgServ;
    private int hour;
    private int min;
    private Uri imgPath;
    private final int PICK_IMAGE_REQUEST = 71;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_serv);

        maxSlider = findViewById(R.id.servMaxSlider);
        minSlider = findViewById(R.id.servMinSlider);
        maxTxt = findViewById(R.id.servMaxRange);
        minTxt = findViewById(R.id.servMinRange);
        maxHrs = findViewById(R.id.serHoraMax);
        minHrs = findViewById(R.id.servHoraMin);
        fab = findViewById(R.id.floatingActionButton);
        titulo = findViewById(R.id.etTitulo);
        desc = findViewById(R.id.etDescripcion);
        txtChoose = findViewById(R.id.txtAddImgProd);
        imgServ = findViewById(R.id.imgServ);

        maxSlider.setMin(100);
        maxSlider.setMax(10000);
        minSlider.setMin(100);
        minSlider.setMax(10000);

        db = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        sReference = fStorage.getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();



        setSlideListeners();

        setDefaultValues();


        showTimePickerDialog();

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
        schedule.put("min",minHrsStr);
        schedule.put("max",maxHrsStr);
        price.put("min",Double.parseDouble(minTxt.getText().toString()));
        price.put("max",Double.parseDouble(maxTxt.getText().toString()));
        images.add(url);
        Offer of = new Offer(true,desc.getText()+"",images,price,titulo.getText()+"","Service",currUser.getUid(),location,1000,schedule);

        db.collection("feed").add(of).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(AddServActiv.this,AddServActiv.this.getResources().getString(R.string.firebaseSuccess),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddServActiv.this,AddServActiv.this.getResources().getString(R.string.firebaseFailure),Toast.LENGTH_SHORT).show();
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
                imgServ.setVisibility(View.VISIBLE);
                imgServ.setImageBitmap(bm);
                imgServ.setAdjustViewBounds(true);

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
        maxHrsStr = "13:00";
        minHrsStr = "12:00";

        minHrs.setText("De "+minHrsStr);
        maxHrs.setText("A "+maxHrsStr);
    }

    private void showTimePickerDialog(){

        minHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddServActiv.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        minHrs.setText("De "+ String.format("%02d",hourOfDay)+":"+String.format("%02d",minute));
                        minHrsStr = String.format("%02d",hourOfDay)+":"+String.format("%02d",minute);
                    }
                },hour, min, DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.show();



            }
        });

        maxHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddServActiv.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        maxHrs.setText("A "+ String.format("%02d",hourOfDay)+":"+String.format("%02d",minute));
                        maxHrsStr = String.format("%02d",hourOfDay)+":"+String.format("%02d",minute);
                    }
                },hour, min, DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.show();
            }

        });
    }

}
