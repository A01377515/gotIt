package mx.itesm.wkt.gotita;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mx.itesm.wkt.gotita.Adapters.BottomNavigationViewHelper;
import mx.itesm.wkt.gotita.Fragments.PersonalProductFrag;
import mx.itesm.wkt.gotita.Fragments.WallFrag;

public class Wall extends AppCompatActivity {

    private void loadWallPage() {
        WallFrag fragWall= new WallFrag();
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.principalLayout, fragWall);
        transaccion.addToBackStack(null);
        transaccion.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadWallPage();
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

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

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
                        loadWallPage();
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wall,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutBtn:
                fbLogout();
                break;
            case R.id.aboutBtn:
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void fbLogout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        finish();
    }
}
