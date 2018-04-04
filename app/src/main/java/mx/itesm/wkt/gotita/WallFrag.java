package mx.itesm.wkt.gotita;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.

 */
public class WallFrag extends Fragment {
    private RecyclerView rvPosts;

    public WallFrag() {
        // Required empty public constructor
    }

    private FirebaseFirestore db;

    //    To catch errors
    private static final String TAG = "FIREBASE";
    private static final String ITEM_DESC = "ITEM_DESC";

    private ArrayList<Offer> offers;

    private void getDataFromFirebase(){
        offers=new ArrayList<>();
        db.collection("feed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());
                                offers.add(document.toObject(Offer.class));
                            }

                            Log.e(TAG,Integer.toString(offers.size())+" elements");

                            for (Offer off : offers) {
                                Log.e(ITEM_DESC,off.getType());
                                Log.e(ITEM_DESC,off.getTitle());
                                Log.e(ITEM_DESC,off.getDescription());
                                Log.e(ITEM_DESC,off.getUser());
                            }

                            AdapterRv adapterRv = new AdapterRv(offers);
                            rvPosts.setAdapter(adapterRv);

                            rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

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

        //Firebase

        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        getDataFromFirebase();

        return v;
    }
}
