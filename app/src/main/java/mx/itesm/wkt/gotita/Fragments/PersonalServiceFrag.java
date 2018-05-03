package mx.itesm.wkt.gotita.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser currUser;

    private ProgressBar progressBar;
    private TextView progressText;


    //    To catch errors
    private static final String TAG = "PERSONAL FRAGMENT";
    private static final String ITEM_DESC = "ITEM_DESC";

    private ArrayList<Offer> offers;

    public PersonalServiceFrag() {
        // Required empty public constructor
    }

    private void createCards(RecyclerView rvPosts){
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);


        AdapterRv adapterRv = new AdapterRv(this.getContext(),offers, NavigationBar.PERSONAL,PersonalServiceFrag.this);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(adapterRv);


        // animation
        rvPosts.setLayoutAnimation(controller);
        rvPosts.getAdapter().notifyDataSetChanged();
        rvPosts.scheduleLayoutAnimation();
    }

    private void getDataFromFirebase(){
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        offers=new ArrayList<>();
        db.collection("feed")
                .whereEqualTo("user",currUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.toObject(Offer.class).getType().equals("Service")) {
                                    offers.add((Offer) document.toObject(Offer.class).withId(document.getId()));
                                }

                            }


                            /*progressBar.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
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
                                    createCards(rvPosts);
                                    rvPosts.setVisibility(View.VISIBLE);
                                }
                            }).translationY(-progressText.getHeight());*/

                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
                            rvPosts.setVisibility(View.VISIBLE);
                            createCards(rvPosts);

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


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Firebase
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        rvPosts.setVisibility(View.INVISIBLE);
        getDataFromFirebase();
    }


}
