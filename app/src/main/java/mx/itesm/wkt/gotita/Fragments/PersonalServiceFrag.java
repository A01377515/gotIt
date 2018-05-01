package mx.itesm.wkt.gotita.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import mx.itesm.wkt.gotita.Adapters.AdapterRv;
import mx.itesm.wkt.gotita.NavigationBar;
import mx.itesm.wkt.gotita.Offer;
import mx.itesm.wkt.gotita.R;

public class PersonalServiceFrag extends Fragment {


    private RecyclerView rvPosts;

    private FirebaseFirestore db;

    private ProgressBar progressBar;
    private TextView progressText;

    //    To catch errors
    private static final String TAG = "PERSONAL FRAGMENT";
    private static final String ITEM_DESC = "ITEM_DESC";

    private ArrayList<Offer> offers;

    public PersonalServiceFrag() {
        // Required empty public constructor
    }

    private void getDataFromFirebase(){
        offers=new ArrayList<>();
        db.collection("feed")
                .whereEqualTo("user","pablitomix")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.toObject(Offer.class).getType().equals("Service")) {
                                    offers.add(document.toObject(Offer.class));
                                }

                            }


                            AdapterRv adapterRv = new AdapterRv(getContext(),offers, NavigationBar.PERSONAL);
                            rvPosts.setAdapter(adapterRv);

                            rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
                            progressBar.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }).translationY(-progressText.getHeight());

                            progressText.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    progressText.setVisibility(View.GONE);
                                }
                            }).translationY(-progressText.getHeight());

                        } else {
                            Log.e(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_personal, container, false);
        rvPosts = v.findViewById(R.id.rvPosts);
        progressBar = v.findViewById(R.id.progressBarPersonal);
        progressText = v.findViewById(R.id.progressTextPersonal);

        //Firebase
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        getDataFromFirebase();
        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);

        return v;
    }


}
