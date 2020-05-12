package com.example.anchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int RC_SIGN_IN = 123;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar mStartPb;

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartPb = findViewById(R.id.welcome_screen_pb);
        //           Initialise the authentication listener to check when user tries to log in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG, "onAuthStateChanged: User already has account");
                    //user is signed in - user already has an account
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    Toast.makeText(MainActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //user is not signed in
                    createSignInIntent();
                }
            }
        };
    }

    //    Sign-in intent
    public void createSignInIntent(){
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d(TAG, "onActivityResult: new user - first time login");
//                    User signed in
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this, "Sign in successful!. Welcome to ANSocial", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"User Signed In" + user.getUid());
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                if (response == null){
                    //User cancelled sign in
                    Toast.makeText(MainActivity.this, "User sign in cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
//                    Device has no network connection
                    Toast.makeText(MainActivity.this, "Device has no network connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
//                    Unknown error occurred
                    Toast.makeText(MainActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                // ...
                Toast.makeText(MainActivity.this, "Sign in failed! Try again", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
