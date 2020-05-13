package com.example.anchat.ui.posts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Posts;
import com.example.anchat.utils.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private List<Posts> postListModels;
    private Context mContext;
    private OnPostItemClicked onPostItemClicked;

    public void setPostListModels(List<Posts> postListModels) {
        this.postListModels = postListModels;
    }

    public PostAdapter(Context context, List<Posts> postListModels) {
        this.postListModels = postListModels;
        this.mContext = context;
    }


    public PostAdapter(OnPostItemClicked onPostItemClicked) {
        this.onPostItemClicked = onPostItemClicked;
    }

    public PostAdapter() {
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        mContext = parent.getContext();
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: group Adapter started bind");
        Posts posts = postListModels.get(position);
        holder.bind(mContext, posts);
    }

    @Override
    public int getItemCount() {
        return postListModels == null ? 0 : postListModels.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView authorName;
        private TextView postTitle;
        private TextView postTimestamp;
        private CircleImageView authorImage;
        private CardView postCardView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_name);
            postTitle = itemView.findViewById(R.id.post_title);
            postTimestamp = itemView.findViewById(R.id.post_timestamp);
            postCardView = itemView.findViewById(R.id.post_item_card);
            authorImage = itemView.findViewById(R.id.author_image);
            postCardView.setOnClickListener(this);

        }

        void bind(Context context, final Posts posts) {
            if (posts.getPostAuthor().getPictureUrl() == null) {
                authorImage.setVisibility(View.GONE);
            } else {
                authorImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(posts.getPostAuthor().getPictureUrl())
                        .centerCrop().override(80, 80).into(authorImage);
            }
            authorName.setText(posts.getPostAuthor().getProfileName());
            postTitle.setText(posts.getPostTitle());
            postTimestamp.setText(DateUtil.getGroupItemString(mContext, posts.getTimestamp()));
        }

        @Override
        public void onClick(View view) {
            onPostItemClicked.onPostClick(getAdapterPosition());

        }
    }

    public interface OnPostItemClicked {
        void onPostClick(int position);
    }
}
