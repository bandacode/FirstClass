package com.example.anchat.ui.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Comments;
import com.example.anchat.data.model.Posts;
import com.example.anchat.data.model.Users;
import com.example.anchat.data.repository.PostsRepo;
import com.example.anchat.utils.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostDetailsFragment extends Fragment {
    private static final String TAG = "PostDetailsFragment";
    private TextView mPostDetailsTitle, mPostDetailsDescription, mPostDetailsDateName, mPostDetailsAuthor;
    private ImageView mPostDetailsImage, mPostDetailsAuthorImage;
    private EditText mWriteComment;
    private Button mAddCommentToPost;
    private PostViewModel mPostViewModel;
    private PostAdapter mPostAdapter;
    private int position;
    private NavController navController;
    private FirebaseFirestore mFirestore;
    private Comments comments;
    private FirebaseUser mUser;
    private Posts mPosts;
    private PostsRepo mPostsRepo;
    private RecyclerView mRecyclerView;
    private CommentAdapter mCommentAdapter;

    public PostDetailsFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated Post Details: Position:" + position);
        super.onViewCreated(view, savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        mRecyclerView = view.findViewById(R.id.comment_recycler_view);
        mCommentAdapter = new CommentAdapter();
        mPosts = new Posts();
        mPostsRepo = new PostsRepo();
        comments = new Comments();
        mPostAdapter = new PostAdapter();
//        Ini text
        mPostDetailsTitle = view.findViewById(R.id.postDetailsTitle);
        mPostDetailsDescription = view.findViewById(R.id.post_detail_desc);
        mPostDetailsDateName = view.findViewById(R.id.post_detail_date_name);
        mPostDetailsAuthor = view.findViewById(R.id.postDetailsAuthorName);
//        Images
        mPostDetailsImage = view.findViewById(R.id.postDetailsImage);
        mPostDetailsAuthorImage = view.findViewById(R.id.comment_author_image);
        mAddCommentToPost = view.findViewById(R.id.button_add_comment);

//        Edit text
        mWriteComment = view.findViewById(R.id.editText_add_comment);

        position = PostDetailsFragmentArgs.fromBundle(getArguments()).getItemPosition();

        setUpRecyclerView();
        mAddCommentToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpPostViewModel();
//        setUpCommentsViewModel();
        System.out.println(comments);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mCommentAdapter);
    }

    private void setUpPostViewModel() {
        mPostViewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        mPostViewModel.getPostsListModelData().observe(getViewLifecycleOwner(), new Observer<List<Posts>>() {
            @Override
            public void onChanged(List<Posts> postsList) {
                Glide.with(getContext())
                        .load(postsList.get(position).getPostImageURL())
                        .centerCrop()
                        .into(mPostDetailsImage);
                mPostDetailsTitle.setText(postsList.get(position).getPostTitle());
                mPostDetailsDateName.setText(DateUtil.getGroupItemString(getContext(), postsList.get(position).getTimestamp()));
                mPostDetailsAuthor.setText(postsList.get(position).getPostAuthor().getProfileName());
                mPostDetailsDescription.setText(postsList.get(position).getPostBody());

                if (postsList.get(position).getPostAuthor().getPictureUrl() == null) {
                    mPostDetailsAuthorImage.setVisibility(View.GONE);
                } else {
                    mPostDetailsAuthorImage.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(postsList.get(position).getPostAuthor().getPictureUrl())
                            .centerCrop().override(80, 80).into(mPostDetailsAuthorImage);
                }
            }
        });

    }

    private void setUpCommentsViewModel() {
        mPostViewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        mPostViewModel.getCommentsListModelData().observe(getViewLifecycleOwner(), new Observer<List<Comments>>() {
            @Override
            public void onChanged(List<Comments> commentsList) {
                mCommentAdapter.setCommentsList(commentsList);
                mCommentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addComment() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = mFirestore.collection("COMMENTS").document();
        if (mUser != null) {
            Glide.with(getContext())
                    .load(mUser.getPhotoUrl())
                    .into(mPostDetailsAuthorImage);
            String newComment = mWriteComment.getText().toString();
            String postId = mPosts.getPostID();
            String commentID = docRef.getId();
            if (newComment.length() == 0) {
                Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

            } else {
                Comments comments = new Comments(newComment, commentID, new Users(mUser), postId);
                Log.d(TAG, "onClick: Post ID" + postId);
                mPostsRepo.addCommentToPosts(mPosts, comments);
                Toast.makeText(getContext(), "Comment Added", Toast.LENGTH_LONG).show();
                mWriteComment.setText("");
            }

        }
    }
}
