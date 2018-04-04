package mx.itesm.wkt.gotita;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class Wall extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_wall:
                    loadWallPage();
                    return true;
                case R.id.navigation_personal:
                    loadPersonalPage();
                    return true;
            }
            return false;
        }
    };

    private void loadPersonalPage() {
        PersonalFrag fragPersonal = new PersonalFrag();
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
