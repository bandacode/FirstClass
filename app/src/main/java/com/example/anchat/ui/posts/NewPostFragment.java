package com.example.anchat.ui.posts;

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
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.anchat.data.repository.PostsRepo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class NewPostFragment extends Fragment {
    private static final String TAG = "NewPostFragment";
    private static final int REQUEST_CODE = 42;
    private EditText postTitle;
    private EditText postBody;
    private ImageView postImage, authorImage, uploadImage;
    private TextView authorName;
    private FloatingActionButton savePost;
    private NavController navController;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser, mUser;
    private FirebaseStorage mFireStorage;
    private StorageReference mStoreReference;
    private Posts posts;
    private PostsRepo postsRepo;
    private Groups groups;
    private PostViewModel mViewModel;
    private int position;
    private ProgressBar newPostPb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save:
//                savePost
                createPosts();
                navController.navigate(R.id.action_nav_new_post_to_nav_group_details);
                Toast.makeText(getContext(), "Post details saved", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_cancel:
//                cancel and navigate back to group details
                navController.navigate(R.id.action_nav_new_post_to_nav_group_details);
                Toast.makeText(getContext(), "Create post cancelled", Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseFirestore = FirebaseFirestore.getInstance();
        mFireStorage = FirebaseStorage.getInstance();
        mStoreReference = mFireStorage.getReference();
        posts = new Posts();
        groups = new Groups();
        postsRepo = new PostsRepo();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        position = NewPostFragmentArgs.fromBundle(getArguments()).getPosition();

        postTitle = view.findViewById(R.id.new_group_title);
        postBody = view.findViewById(R.id.new_group_desc);
        uploadImage = view.findViewById(R.id.upload_group_image);
        postImage = view.findViewById(R.id.new_group_image);
        authorImage = view.findViewById(R.id.new_group_user_image);
        authorName = view.findViewById(R.id.text_view_author_name);
        navController = Navigation.findNavController(view);
        newPostPb = view.findViewById(R.id.new_group_pb);

        Glide.with(getContext())
                .load(mUser.getPhotoUrl())
                .into(authorImage);


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage.setVisibility(View.GONE);
                newPostPb.setVisibility(View.VISIBLE);
                Intent intentImageUpload = new Intent(Intent.ACTION_GET_CONTENT);
                intentImageUpload.setType("image/*");
                intentImageUpload.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intentImageUpload.createChooser(intentImageUpload,
                        "Insert Picture"), REQUEST_CODE);
            }
        });

    }

    private void createPosts(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){

            Glide.with(getContext())
                    .load(firebaseUser.getPhotoUrl())
                    .into(authorImage);
            String postImage = posts.getPostImageURL();
            String postText = postTitle.getText().toString();
            String postBodyText = postBody.getText().toString();
            String postID = posts.getPostID();
            String groupID = groups.getGroupId();
            if (postBodyText.length() != 0 && postText.length() != 0){
                Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Posts posts = new Posts(new Users(firebaseUser), postID, postText, postBodyText, postImage, groupID);
                postsRepo.addPostData(posts);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        final Uri postImage = data.getData();
        assert postImage != null;
        final StorageReference postImagesRef = mStoreReference.child("group_images");
        final UploadTask uploadTask = postImagesRef.putFile(postImage);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return postImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                Log.d(TAG, "onComplete: Image Upload Successful");
                Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                if (downloadUri != null){
                    String photoStringLink = downloadUri.toString();
                    String imageName = postImagesRef.getPath();

                    posts.setPostImageURL(photoStringLink);
                    showImage(photoStringLink);
                    Log.d(TAG, "onComplete: show url" + photoStringLink + imageName);

                }
            }
        });
    }


    private void showImage(String url){
        if (url != null && !url.isEmpty()){
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Glide.with(this)
                    .load(url)
                    .override(300,150)
                    .into(postImage);
        }
    }


}
