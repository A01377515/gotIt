package mx.itesm.wkt.gotita;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFrag extends Fragment {

    private RecyclerView rvPosts;

    public PersonalFrag() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal, container, false);
        rvPosts = v.findViewById(R.id.rvPosts);
        String[] arrT = {"Titulo", "B","C"};
        String[] arrD = {"Precio", "Y","Z"};
        AdapterRv adapterRv = new AdapterRv(arrT, arrD);
        rvPosts.setAdapter(adapterRv);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }


}
