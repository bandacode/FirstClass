package com.example.anchat.ui.home;


import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

;

import com.example.anchat.data.model.Groups;
import com.example.anchat.data.model.Users;
import com.example.anchat.data.repository.Firestore;
import com.example.anchat.data.repository.GroupsRepo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class GroupViewModel extends ViewModel implements GroupsRepo.FireStoreTaskComplete {
    private static final String TAG = "GroupViewModel";
    public FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private GroupsRepo groupsRepo = new GroupsRepo(this);

    private MutableLiveData<List<Groups>> groupListModelData = new MutableLiveData<>();


    public LiveData<List<Groups>> getGroups(){
        if (groupListModelData == null){
            groupListModelData = new MutableLiveData<>();

        }
        return groupListModelData;

    }


    public GroupViewModel(){
        Log.d(TAG, "GroupViewModel: viewModel Instantiated");
        groupsRepo.loadGroups();
    }


    @Override
    public void getGroupData(List<Groups> groupList) {
        groupListModelData.setValue(groupList);
    }

    @Override
    public void onError(Exception e) {

    }

}