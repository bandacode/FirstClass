package com.example.anchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anchat.data.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.anchat.EditProfileActivity.IMAGE_URL;
import static com.example.anchat.EditProfileActivity.USER_BIO;

public class ProfileActivity extends AppCompatActivity {
    private TextView mAuthorName, mEmailAddress, mUserBio;
    private ImageView mUserImage;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseAuth.AuthStateListener mAuthListener;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private Users mUsers = new Users();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseUser = mFirebaseAuth.getCurrentUser();


        mAuthorName = findViewById(R.id.fullname);
        mEmailAddress = findViewById(R.id.user_profile_email);
        mUserBio = findViewById(R.id.bio_user);
        mUserImage = findViewById(R.id.profilepic);

        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater  = getMenuInflater();
        menuInflater.inflate(R.menu.profile, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_profile:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete_profile:
                deleteUserAccount();
                finish();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                Toast.makeText(this, "Profile successfully deleted", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
        /*    DocumentReference docRef = db.collection("USERS").document();

            Log.i("User", "Check user details = " + FirebaseAuth.getInstance().getCurrentUser());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            String nameText = mUsers.getProfileName();
                            String emailText = mUsers.getEmailAddress();
                            String bioText = mUsers.getAboutUser();
                            String imageUrlText = mUsers.getPictureUrl();

                            mAuthorName.setText(nameText);
                            mEmailAddress.setText(emailText);
                            mUserBio.setText(bioText);
                            showImage(imageUrlText);
                        } else {
                            Log.d("TAG", "No such document");

                        }


                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });*/
            mAuthorName.setText(name);
            mEmailAddress.setText(email);

            Glide.with(this)
                    .load(photoUrl)
                    .into(mUserImage);
        }

    }

    public void showImage(String url){
        if (url != null && !url.isEmpty()){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Glide.with(this)
                    .load(url)
                    .override(width,200)
                    .into(mUserImage);
        }
    }

    public void deleteUserAccount(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User account deleted.");

                            }
                        }
                    });
        } else
            Log.d("TAG", "Account not deleted");
        startActivity(new Intent(this, HomeActivity.class));
        Toast.makeText(this, "Hey there", Toast.LENGTH_LONG).show();


    }
//    public void deleteUser() {
//        // [START delete_user]
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        user.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User account deleted.");
//                        }
//                    }
//                });
//        // [END delete_user]
//    }
}


