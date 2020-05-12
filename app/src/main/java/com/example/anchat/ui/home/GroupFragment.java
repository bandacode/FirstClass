package com.example.anchat.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anchat.R;
import com.example.anchat.data.model.Groups;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GroupFragment extends Fragment implements GroupAdapter.OnGroupItemClicked {
    private static final String TAG = "GroupFragment";
    private ProgressBar mProgressBar;
    private ConstraintLayout mEmptyMessageContainer;
    private GroupViewModel mGroupViewModel;
    private RecyclerView mRecyclerView;
    private GroupAdapter mGroupAdapter;
    private FloatingActionButton mFab;
    private Groups mGroups;
    private NavController navController, groupDetailsNavController;
    private Animation fadeInAnim;
    private Animation fadeOutAnim;


    private GroupViewModel groupViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_user_profile:
//                open user profile
                groupDetailsNavController.navigate(R.id.action_nav_home_to_userProfileFragment);
                Toast.makeText(getContext(), "User Profile", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_sign_out:
//                cancel and navigate back to group details
                signOut();
                Toast.makeText(getContext(), "Home", Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_group, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.group_recycler_view);
        mFab = view.findViewById(R.id.fab_add_new_group);
        mGroupAdapter = new GroupAdapter(this);
        mGroups = new Groups();
        setUpRecyclerView();

        groupDetailsNavController = Navigation.findNavController(view);

        navController = Navigation.findNavController(view);

        fadeInAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);


        mProgressBar = view.findViewById(R.id.group_list_pb);
        mEmptyMessageContainer = view.findViewById(R.id.empty_message_container);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigate to NewGroupFragment
                navController.navigate(R.id.action_nav_home_to_newGroupFragment);

            }
        });


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGroupViewModel = new ViewModelProvider(getActivity()).get(GroupViewModel.class);
        mGroupViewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<Groups>>() {
            @Override
            public void onChanged(List<Groups> groupsList) {
                mRecyclerView.setAnimation(fadeInAnim);
                mProgressBar.setAnimation(fadeOutAnim);

                updateUI(groupsList);

                mGroupAdapter.setGroupListModels(groupsList);
                mGroupAdapter.notifyDataSetChanged();

            }
        });

    }
    private void updateUI(List<Groups> groups){
        if (groups == null || groups.size() == 0){
            mEmptyMessageContainer.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);

        } else {
            mEmptyMessageContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGroupAdapter.setGroupListModels(groups);
        }
    }


    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mGroupAdapter);
    }

    @Override
    public void onGroupClick(int position) {
        GroupFragmentDirections.ActionHomeFragmentToHomeSecondFragment action =
                GroupFragmentDirections.actionHomeFragmentToHomeSecondFragment();
        action.setPosition(position);
        groupDetailsNavController.navigate(action);

    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("Logout", "User logged out");
                    }
                });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
