package mx.itesm.wkt.gotita.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
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


/**
 * A simple {@link Fragment} subclass.

 */
public class WallFrag extends Fragment {

    private RecyclerView rvPosts;

    private FirebaseFirestore db;

    private ProgressBar progressBar;
    private TextView progressText;
    private ImageView imgEmpty;
    private SwipeRefreshLayout swipe;

    //    To catch errors
    private static final String TAG = "FIREBASE";
    private static final String ITEM_DESC = "ITEM_DESC";

    private ArrayList<Offer> offers;

    public WallFrag() {
        // Required empty public constructor
    }

    private void createCards(RecyclerView rvPosts){
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);


        AdapterRv adapterRv = new AdapterRv(this.getContext(),offers, NavigationBar.WALL,WallFrag.this);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(adapterRv);


        // animation
        rvPosts.setLayoutAnimation(controller);
        rvPosts.getAdapter().notifyDataSetChanged();
        rvPosts.scheduleLayoutAnimation();
    }


    private void getDataFromFirebase(){
        offers=new ArrayList<>();
        db.collection("feed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.e(TAG, document.getId() + " => " + document.getData());
                                if(document.toObject(Offer.class).isActive()){
                                    offers.add(document.toObject(Offer.class));
                                }
                            }


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
                                    if (offers.size() == 0){
                                        imgEmpty.setVisibility(View.VISIBLE);
                                        imgEmpty.setAlpha(0.0f);
                                        imgEmpty.animate().alpha(1.0f);

                                    }else{
                                        imgEmpty.setVisibility(View.GONE);
                                    }
                                    createCards(rvPosts);
                                    swipe.setRefreshing(false);
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
        View v = inflater.inflate(R.layout.fragment_wall, container, false);
        rvPosts = v.findViewById(R.id.rvPosts);
        progressBar = v.findViewById(R.id.progressBarWall);
        progressText = v.findViewById(R.id.progressTextWall);
        imgEmpty = v.findViewById(R.id.imgEmptyWall);

        swipe = v.findViewById(R.id.swipeWall);
        swipe.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark),getResources().getColor(R.color.colorPrimary));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getDataFromFirebase();

            }
        });

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
