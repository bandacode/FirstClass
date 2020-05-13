package com.example.anchat.ui.home;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Groups;
import com.example.anchat.data.model.Posts;
import com.example.anchat.data.model.Users;
import com.example.anchat.data.repository.Firestore;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewGroupFragment extends Fragment {
    private static final int REQUEST_CODE = 42;
    private static final String TAG = "NewGroupFragment";
    private FirebaseFirestore mFiresbaseFirestore;
    private FirebaseStorage mFireStorage;
    private StorageReference mStoreReference;
    private ImageView mGroupImage;
    private EditText mGroupTitle, mGroupDesc;
    private ImageView mUploadGroupImage, mUserImage;
    private FirebaseUser mUser;

    private Firestore mFirestoreRepo;
    private FirebaseUser firebaseUser;
    private NavController navController;

    private Groups mGroupDetails, group1;
    private Users userDetails;
    private Posts mPosts;


    public NewGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
//                saveGroup
                addGroupToFirestore();
                navController.navigate(R.id.action_nav_new_group_to_nav_home);
                Toast.makeText(getContext(), "Group details saved", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_cancel:
//                cancel and navigate back to group details
                navController.navigate(R.id.action_nav_new_group_to_nav_home);
                Toast.makeText(getContext(), "Create group cancelled", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Create Group");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_group, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFiresbaseFirestore = FirebaseFirestore.getInstance();
        mFireStorage = FirebaseStorage.getInstance();
        mStoreReference = mFireStorage.getReference();


        userDetails = new Users();
        mPosts = new Posts();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mGroupDetails = new Groups();
        group1 = new Groups();
        mFirestoreRepo = new Firestore();
        navController = Navigation.findNavController(view);


        mGroupImage = view.findViewById(R.id.new_group_image);
        mGroupTitle = view.findViewById(R.id.new_group_title);
        mGroupDesc = view.findViewById(R.id.new_group_desc);
        mUploadGroupImage = view.findViewById(R.id.upload_group_image);
        mUserImage = view.findViewById(R.id.new_group_user_image);

        mUploadGroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentImageUpload = new Intent(Intent.ACTION_GET_CONTENT);
                intentImageUpload.setType("image/*");
                intentImageUpload.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intentImageUpload.createChooser(intentImageUpload,
                        "Insert Picture"), REQUEST_CODE);
            }
        });

        Glide.with(requireContext())
                .load(mUser.getPhotoUrl())
                .into(mUserImage);


    }

    private void addGroupToFirestore() {
        Log.d(TAG, "addGroupToFirestore: called");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            String title = mGroupTitle.getEditableText().toString().trim();
            String desc = mGroupDesc.getText().toString();
            String image = mGroupDetails.getGroupImageUrl();
            String userID = firebaseUser.getUid();
            if (title.length() == 0 && desc.length() == 0) {
                Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                mGroupDetails = new Groups(title, desc, image, userID);
                Firestore.getGroupsReference(mFiresbaseFirestore).add(mGroupDetails).
                        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        final Uri groupImage = data.getData();
        assert groupImage != null;
        final StorageReference groupImagesRef = mStoreReference.child("group_images/");
        final UploadTask uploadTask = groupImagesRef.putFile(groupImage);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return groupImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                Log.d(TAG, "onComplete: Image Upload Successful");
                Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                if (downloadUri != null) {
                    String photoStringLink = downloadUri.toString();
                    String imageName = groupImagesRef.getPath();

                    mGroupDetails.setGroupImageUrl(photoStringLink);
                    showImage(photoStringLink);
                    Log.d(TAG, "onComplete: show url" + photoStringLink + imageName);

                }
            }
        });
    }

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Glide.with(this)
                    .load(url)
                    .override(300, 200)
                    .into(mGroupImage);
        }
    }


}

//    Navigate Action
