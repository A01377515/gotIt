package mx.itesm.wkt.gotita;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import mx.itesm.wkt.gotita.Adapters.BottomNavigationViewHelper;
import mx.itesm.wkt.gotita.Fragments.PersonalProductFrag;
import mx.itesm.wkt.gotita.Fragments.WallFrag;

public class Wall extends AppCompatActivity {

    private void loadPersonalPage() {
        PersonalProductFrag fragPersonal = new PersonalProductFrag();
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.principalLayout, fragPersonal);
        transaccion.addToBackStack(null);
        transaccion.commit();
    }

    private void loadWallPage() {
        WallFrag fragWall= new WallFrag();
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.principalLayout, fragWall);
        transaccion.addToBackStack(null);
        transaccion.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_personal:
                        Intent intent0 = new Intent(Wall.this, TabbedActiv.class);
                        startActivity(intent0);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_wall:

                        break;
                }

                return false;
            }
        });
        loadWallPage();
    }

}
