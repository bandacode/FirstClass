package com.example.anchat.ui.posts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Comments;
import com.example.anchat.utils.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private static final String TAG = "CommentAdapter";
    private Context mContext;
    private List<Comments> commentsList;

    public void setCommentsList(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }
    public CommentAdapter(){}


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        mContext = parent.getContext();
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: comment data bind");
        Comments comments = commentsList.get(position);
        holder.bind(mContext, comments);


    }

    @Override
    public int getItemCount() {
        return commentsList == null ? 0 : commentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView mAuthorName;
        private TextView mCommentTimestamp;
        private TextView mComment;
        private CircleImageView authorImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorName = itemView.findViewById(R.id.comment_username);
            mComment = itemView.findViewById(R.id.comment_content);
            mCommentTimestamp = itemView.findViewById(R.id.comment_date);
            authorImage = itemView.findViewById(R.id.comment_author_image);
        }
        void bind(Context context, final Comments comments){
            if (comments.getCommentsAuthor().getPictureUrl() == null){
                authorImage.setVisibility(View.GONE);
            } else {
                authorImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(comments.getCommentsAuthor().getPictureUrl())
                        .centerCrop().override(80,80).into(authorImage);
            }
            mAuthorName.setText(comments.getCommentsAuthor().getProfileName());
            mComment.setText(comments.getCommentText());
            mCommentTimestamp.setText(DateUtil.getGroupItemString(mContext, comments.getCommentTimestamp()));
        }
    }
}
