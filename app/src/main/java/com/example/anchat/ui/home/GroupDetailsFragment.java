package com.example.anchat.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Groups;
import com.example.anchat.data.model.Posts;
import com.example.anchat.ui.posts.PostAdapter;
import com.example.anchat.ui.posts.PostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupDetailsFragment extends Fragment implements PostAdapter.OnPostItemClicked {
    private static final String TAG = "GroupDetailsFragment";
    private GroupViewModel mGroupViewModel;
    private NavController navController, newPostSNavController;
    private int position;
    private FloatingActionButton mFab;
    //    Group Details header
    private ImageView mImageView;
    private TextView mGroupName;
    private ProgressBar mProgressBar;
    private ConstraintLayout mEmptyMessageContainer;
    private PostViewModel mPostViewModel;
    private CardView mCardView;
    private NavController mPostDetailsNavController;


    //    Post details list
    private RecyclerView mRecyclerView;
    private PostAdapter mPostAdapter;


    public GroupDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Position:" + position);

        mRecyclerView = view.findViewById(R.id.group_details_recycler_view);
        mPostAdapter = new PostAdapter(this);

        setUpRecyclerView();
        mProgressBar = view.findViewById(R.id.group_details_pb);
        mEmptyMessageContainer = view.findViewById(R.id.empty_message_container_groupDetails);

        navController = Navigation.findNavController(view);
        newPostSNavController = Navigation.findNavController(view);
        mPostDetailsNavController = Navigation.findNavController(view);
        position = GroupDetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        mGroupName = view.findViewById(R.id.group_details_title);
        mImageView = view.findViewById(R.id.group_details_image);
        mFab = view.findViewById(R.id.fab_create_post_in_group);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GroupDetailsFragmentDirections.ActionNavGroupDetailsToNavNewPost action =
                        GroupDetailsFragmentDirections.actionNavGroupDetailsToNavNewPost();
                action.setPosition(position);
                navController.navigate(action);

            }
        });


    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mPostAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setGroupViewModel();
    }

    private void updateUI(List<Posts> postsList) {
        if (postsList == null || postsList.size() == 0) {
            mEmptyMessageContainer.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyMessageContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setGroupViewModel() {
        Log.d(TAG, "setGroupViewModel: initialized");
        mGroupViewModel = new ViewModelProvider(getActivity()).get(GroupViewModel.class);
        mGroupViewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<Groups>>() {
            @Override
            public void onChanged(List<Groups> groupsList) {
                Glide.with(getContext())
                        .load(groupsList.get(position).getGroupImageUrl())
                        .centerCrop()
                        .override(300, 200)
                        .into(mImageView);

                mGroupName.setText(groupsList.get(position).groupName);
                setPostViewModel(groupsList.get(position).groupId);
            }
        });
    }

    private void setPostViewModel(final String groupId) {
        Log.d(TAG, "setPostViewModel: initialized");
        mPostViewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        mPostViewModel.getPostsListModelData().observe(getViewLifecycleOwner(), new Observer<List<Posts>>() {
            @Override
            public void onChanged(List<Posts> postsList) {
                ArrayList<Posts> posts = new ArrayList<>();
                for (Posts post : postsList) {
                    boolean equal = post.getGroupId().replaceAll("\\P{Print}", "").trim().equalsIgnoreCase(groupId.replaceAll("\\P{Print}", "").trim());
                    Log.d(TAG, "THEY ARE EQUAL: " + equal + " " + post.getGroupId() + " ? " + groupId);
                    if (equal) {
                        Log.d(TAG, "Added to Posts");
                        posts.add(post);
                    }
                }
                updateUI(posts);
                mPostAdapter.setPostListModels(posts);
                mPostAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPostClick(int position) {
        GroupDetailsFragmentDirections.ActionNavGroupDetailsToNavPostDetails action =
                GroupDetailsFragmentDirections.actionNavGroupDetailsToNavPostDetails();
        action.setItemPosition(position);
        mPostDetailsNavController.navigate(action);
    }
}
