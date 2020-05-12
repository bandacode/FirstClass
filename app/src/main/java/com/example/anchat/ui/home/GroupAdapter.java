package com.example.anchat.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Groups;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private static final String TAG = "GroupAdapter";
    private List<Groups> groupListModels;
    private Context mContext;
    private static OnGroupItemClicked onGroupItemClicked;




    public void setGroupListModels(List<Groups> groupListModels) {
        this.groupListModels = groupListModels;
    }

    public GroupAdapter(OnGroupItemClicked onGroupItemClicked){
        this.onGroupItemClicked = onGroupItemClicked;

    }

    public GroupAdapter(Context context, List<Groups> groupListModels) {
        this.groupListModels = groupListModels;
        this.mContext = context;
    }

    public GroupAdapter() {}

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        mContext = parent.getContext();
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: group Adapter started bind");
//        holder.groupTitle.setText(groupListModels.get(position).getGroupName());
        Groups groups = groupListModels.get(position);
        holder.bind(mContext, groups);
    }


    @Override
    public int getItemCount() {
        return groupListModels == null ? 0 : groupListModels.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groupTitle;
        private TextView groupDesc;
        private CircleImageView groupImage;
        private Button viewGroup;
        private CardView groupDetailsCardView;


        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.group_title);
            groupDesc = itemView.findViewById(R.id.group_description);
            groupImage = itemView.findViewById(R.id.group_image);
            groupDetailsCardView = itemView.findViewById(R.id.group_item);
            viewGroup = itemView.findViewById(R.id.view_group_button);
            viewGroup.setOnClickListener(this);

        }

        @SuppressLint("SetTextI18n")
        public void bind(Context context, final Groups group) {
            if (group.getGroupImageUrl() == null) {
                groupImage.setVisibility(View.GONE);
            } else {
                groupImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(group.getGroupImageUrl())
                        .centerCrop().override(80, 80).into(groupImage);

            }
            groupTitle.setText(group.getGroupName());
            groupDesc.setText(group.getGroupDescription());

        }

        @Override
        public void onClick(View view) {
            onGroupItemClicked.onGroupClick(getAdapterPosition());
        }
    }
    public interface OnGroupItemClicked{
        void onGroupClick(int position);
    }
}
