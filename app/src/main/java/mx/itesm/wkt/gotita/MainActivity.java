package mx.itesm.wkt.gotita;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    TextViews
    private TextView tvTitle;

//    To catch errors
    private static final String TAG = "FacebookLogin";

//    Firebase Auth
    private FirebaseAuth mAuth;

//    Facebook Auth
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private CallbackManager mCallbackManager;

    //test
    final MainActivity app=this;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Custom Font
        Typeface font_roboto = Typeface.createFromAsset(getAssets(),  "font/Roboto-Regular.ttf");

        tvTitle=findViewById(R.id.main_title);

//        Set Custom Font
        tvTitle.setTypeface(font_roboto);

//        Firebase Auth
        mAuth=FirebaseAuth.getInstance();

//        Facebook Auth
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.main_login_btn);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS));
        loginButton.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(app, "Bienvenido a gotIt", Toast.LENGTH_LONG).show();
                        if(loginResult.getAccessToken()!=null) handleFacebookAccessToken(loginResult.getAccessToken());
                        else Toast.makeText(app, "MASSIVE ERROR", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(app, "Loggeado Cancelado", Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(app, "Loggeado incorrecto", Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(app, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void prueba(View v){
        Intent intWall = new Intent(this,Wall.class);
        startActivity(intWall);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intWall = new Intent(this, Wall.class);
            startActivity(intWall);
            Log.e("start activity","t");
        } else {
            Log.e("updateUI","yes");
            tvTitle.setText("Got It");
        }
    }


}
