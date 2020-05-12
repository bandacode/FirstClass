package com.example.anchat.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.anchat.data.model.Groups;
import com.example.anchat.data.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GroupsRepo {
    private static final String TAG = "GroupsRepo";
    private FireStoreTaskComplete fireStoreTaskComplete;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("GROUPS");

    public GroupsRepo(FireStoreTaskComplete fireStoreTaskComplete){
        this.fireStoreTaskComplete = fireStoreTaskComplete;

    }
    public GroupsRepo(){}



//    Our public method to query the database and pass the data into the ViewModel
    public void loadGroups() {
        Log.d(TAG, "loadGroups: called");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        if (document.exists()){
                            fireStoreTaskComplete.getGroupData((task.getResult().toObjects(Groups.class)));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }else {
                            fireStoreTaskComplete.onError(task.getException());
                        }
                    }
                }

            }
        });
    }

//    public interface to send data from GroupRepo(Firebase) to PostViewModel
//    this data is pushed to the View from the ViewModel(Fragment)
    public interface FireStoreTaskComplete{
        void getGroupData(List<Groups> groupList);
        void onError(Exception e);
    }


}
