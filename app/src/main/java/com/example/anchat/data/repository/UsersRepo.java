package com.example.anchat.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.anchat.data.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UsersRepo {
    private static final String TAG = "UsersRepo";
     private UserFirestoreTaskComplete userFirestoreTaskComplete;


     private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

     public UsersRepo (){}

     public UsersRepo(UserFirestoreTaskComplete userFirestoreTaskComplete){
         this.userFirestoreTaskComplete = userFirestoreTaskComplete;
     }


     public void getData(){
         final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         if (user != null){
             final String userID = user.getUid();
             DocumentReference docRef = firestore.collection("USERS").document(userID);
             docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                     if (task.isSuccessful() && task.getResult() != null){
                         DocumentSnapshot document = task.getResult();
                         if (document.exists()){
                             userFirestoreTaskComplete.getUserData(task.getResult().toObject(Users.class));
                             Log.d(TAG, document.getId() + " => " + document.getData());

                         } else {
                             userFirestoreTaskComplete.onError(task.getException());
                         }
                     }
                 }
             });
         }
     }

    public interface UserFirestoreTaskComplete{
        void getUserData(Users userData);
        void onError(Exception e);

    }
}
