package mx.itesm.wkt.gotita;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;



import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import mx.itesm.wkt.gotita.Adapters.BottomNavigationViewHelper;
import mx.itesm.wkt.gotita.Adapters.SectionsPageAdapter;
import mx.itesm.wkt.gotita.Fragments.PersonalProductFrag;
import mx.itesm.wkt.gotita.Fragments.PersonalServiceFrag;

public class TabbedActiv extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);



        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_personal:

                        break;

                    case R.id.navigation_wall:
                        finish();
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalProductFrag(),"Productos");
        adapter.addFragment(new PersonalServiceFrag(),"Servicios");
        viewPager.setAdapter(adapter);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void loadPersonalPage() {
        PersonalProductFrag fragPersonal = new PersonalProductFrag();
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.container, fragPersonal);
        transaccion.addToBackStack(null);
        transaccion.commit();
    }



}
