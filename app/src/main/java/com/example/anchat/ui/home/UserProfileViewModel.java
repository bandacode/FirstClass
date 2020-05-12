package com.example.anchat.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anchat.data.model.Users;
import com.example.anchat.data.repository.Firestore;
import com.example.anchat.data.repository.UsersRepo;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileViewModel extends ViewModel implements UsersRepo.UserFirestoreTaskComplete {
    private static final String TAG = "UserProfileViewModel";
    // TODO: Implement the ViewModel
    public FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private MutableLiveData<Users> userDataModel = new MutableLiveData<>();
    private UsersRepo usersRepo = new UsersRepo(this);

    public LiveData<Users> getUserData(){
        if (userDataModel == null){
            userDataModel = new MutableLiveData<>();
        }
        return userDataModel;

    }

    public UserProfileViewModel(){
        Log.d(TAG, "UserProfileViewModel: instantiated");
//        Get user data from repo
        usersRepo.getData();



    }

    @Override
    public void getUserData(Users userData) {

    }

    @Override
    public void onError(Exception e) {

    }
}
