package com.example.anchat.ui.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anchat.R;
import com.example.anchat.data.model.Posts;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostFragment extends Fragment {
    private static final String TAG = "PostListFragment";
    private ProgressBar mProgressBar;
    private ConstraintLayout mEmptyMessageContainer;
    private RecyclerView mRecyclerView;
    private PostAdapter mPostAdapter;
    private CardView mPostCardView;
    private Posts mPosts;
    private PostViewModel postViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_posts, container, false);
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.post_feed_recycler_view);
        mPostAdapter = new PostAdapter();
        mPosts = new Posts();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mPostAdapter);

        mProgressBar = view.findViewById(R.id.post_list_pb);
        mEmptyMessageContainer = view.findViewById(R.id.empty_message_container_posts);
    }

    private void updateUI(List<Posts> posts){
        if (posts == null || posts.size() == 0){
            mEmptyMessageContainer.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);

        } else {
            mEmptyMessageContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mPostAdapter.setPostListModels(posts);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: from HomeFeed called");
        super.onActivityCreated(savedInstanceState);

        PostViewModel postViewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        postViewModel.getPostsListModelData().observe(getViewLifecycleOwner(), new Observer<List<Posts>>() {
            @Override
            public void onChanged(List<Posts> postListModels) {
                Log.d(TAG, "onChanged: called");
                updateUI(postListModels);
                mPostAdapter.setPostListModels(postListModels);
                mPostAdapter.notifyDataSetChanged();
            }
        });
    }
}
