package com.example.anchat.ui.home;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anchat.R;
import com.example.anchat.data.model.Users;


public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    private TextView userName;
    private TextView userEmail;
    private ImageView userImage;
    private NavController navController;
    private String nameOfAuthor;
    private String emailOfAuthor;

    private UserProfileViewModel mViewModel;


    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_profile:
//                open user profile
                navController.navigate(R.id.action_nav_user_profile_to_nav_home);
                Toast.makeText(getContext(), "Edit profile", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_delete_profile:
//                cancel and navigate back to group details
                navController.navigate(R.id.action_nav_user_profile_to_nav_home);
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = view.findViewById(R.id.fullname);
        userEmail = view.findViewById(R.id.user_email);
        userImage = view.findViewById(R.id.profilepic);


        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(UserProfileViewModel.class);
        mViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<Users>() {
            @Override
            public void onChanged(Users users) {
                Glide.with(getContext())
                        .load(users.getPictureUrl())
                        .centerCrop()
                        .override(300, 200)
                        .into(userImage);

                userName.setText(users.getProfileName());
                userEmail.setText(users.getEmailAddress());
            }
        });
        // TODO: Use the ViewModel
    }

}
