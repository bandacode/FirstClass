package com.example.anchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anchat.data.model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private static final int REQUEST_CODE = 42;
    public static final String IMAGE_URL = "imageURL";
    public static final String USER_BIO = "userBio";
    private static final String FULL_NAME = "profileName";

    private TextView mChooseImage, mEditProfile, mName, mAbout;
    private ImageView mImage;
    private EditText mFullName, mAboutUser;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String imageUrl;
    private FirebaseUser firebaseUser;
    private Users user = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        Text
        mEditProfile = findViewById(R.id.edit_profile);
        mChooseImage = findViewById(R.id.button_choose_an_image);
        mName = findViewById(R.id.name_user_edit);
        mAbout = findViewById(R.id.about_user_edit);
//        Image
        mImage = findViewById(R.id.edit_image);

//        Edit
        mFullName = findViewById(R.id.edit_name);
        mAboutUser = findViewById(R.id.edit_about);

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfilePicUpload = new Intent(Intent.ACTION_GET_CONTENT);
                intentProfilePicUpload.setType("image/*");
                intentProfilePicUpload.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intentProfilePicUpload,
                        "Insert Picture"), REQUEST_CODE);
            }
        });


        populateEditText();
        showImage(getImageUrl());



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_menu_save:
                saveProfile();
                finish();
                Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
                backToProfile();
                return true;
            case R.id.action_update_profile:
                updateProfile();
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show();
                backToProfile();
                return true;
            case R.id.edit_menu_cancel:
                Toast.makeText(this, "Edit operation cancelled", Toast.LENGTH_SHORT).show();
                clean();
                backToProfile();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    Activity Result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
//            Upload from local storage
            final Uri imageUri = data.getData();
            assert imageUri != null;
            final StorageReference ref = mStorageRef.child("user_profile_photos/" + imageUri.getLastPathSegment());
            final UploadTask uploadTask = ref.putFile(imageUri);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(EditProfileActivity.this, "Upload Failed ", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d("Upload", "Image upload successful");
                    Toast.makeText(EditProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            });
            // [END upload_file]

//            Get a download URL
            Task<Uri> getDownloadUrlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return ref.getDownloadUrl();
                }
            });

            getDownloadUrlTask.addOnCompleteListener(EditProfileActivity.this, new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        System.out.println("Upload " + downloadUri);
                        Log.d("Upload", "Image upload successful");
                        assert downloadUri != null;
                        String photoStringLink = downloadUri.toString(); //get download url
                        setImageUrl(photoStringLink);

                        Log.d("Url:", photoStringLink);
                        showImage(photoStringLink);


                    }

                }
            });
        }
    }
    public void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(this)
                    .load(url)
                    .override(150, 150)
                    .into(mImage);

        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private void clean() {
        mFullName.setText("");
        mAboutUser.setText("");
    }

    private void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String fullNameText = mFullName.getText().toString();
            String image = getImageUrl();


            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fullNameText)
                    .setPhotoUri(Uri.parse(image))
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
            user.updateEmail("user@example.com")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });
        }


    }

    private void backToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void saveProfile() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
       final FirebaseUser saveUser = FirebaseAuth.getInstance().getCurrentUser();
        final StorageReference ref = mStorageRef.child("user_profile_photos/");
        DocumentReference docRef = firestore.collection("USERS").document();
        if (saveUser != null){

            String uid = saveUser.getUid();
            String fullNameText = saveUser.getDisplayName();
            String emailAddress = saveUser.getEmail();
            String aboutUserText = mAboutUser.getText().toString();
            String userImage = ref.getDownloadUrl().toString();

            assert fullNameText != null;
            if (fullNameText.length() == 0 && aboutUserText.length() == 0){
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_LONG).show();

            } else {
                Users users = new Users(uid,fullNameText,userImage,emailAddress,aboutUserText);
                Toast.makeText(this, "User Saved", Toast.LENGTH_LONG).show();
                docRef.set(users);
                clean();

            }
        } else {
            Toast.makeText(this, "Could not save user data", Toast.LENGTH_LONG).show();
        }



    }

    public void populateEditText() {
        final FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        if (users != null){
            String userId = users.getUid();
            String nameText = users.getDisplayName();
            String aboutUserText = user.getAboutUser();
            Uri photoUrl = users.getPhotoUrl();

            mFullName.setText(nameText);
            mAboutUser.setText(aboutUserText);
            Glide.with(this)
                    .load(photoUrl)
                    .into(mImage);




        }
    }



}
