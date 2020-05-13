package com.example.anchat.data.repository;

import android.media.MediaDrm;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.anchat.data.model.Comments;
import com.example.anchat.data.model.Groups;
import com.example.anchat.data.model.Posts;
import com.example.anchat.ui.home.GroupDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;


public class PostsRepo {
    private static final String TAG = "PostsRepo";
    private OnFireStoreTaskComplete onFireStoreTaskComplete;
    private commentsFirestoreTask mCommentsFirestoreTask;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("POSTS");
    private CollectionReference commentCollection = firestore.collection("COMMENTS");
    private Posts posts = new Posts();
    private Groups groups = new Groups();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Comments comments = new Comments();

    public PostsRepo(){}


    public PostsRepo(commentsFirestoreTask mCommentsFirestoreTask){
        this.mCommentsFirestoreTask = mCommentsFirestoreTask;
    }

    public PostsRepo(OnFireStoreTaskComplete onFireStoreTaskComplete){
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;

    }
//    Method to retrieve data from database
    public  void getPostData(){
        Log.d(TAG, "getPostData: called");
        collectionReference.
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        if (document.exists()){
                            onFireStoreTaskComplete.postDataAdded(Objects.requireNonNull(task.getResult()).toObjects(Posts.class));
                            Log.d(TAG, document.getId() + " => " + document.getData());

                        }else {
                            onFireStoreTaskComplete.onError(task.getException());
                        }
                    }

                }
            }
        });
    }
    public void addPostData(final Posts posts){
        Log.d(TAG, "addPostData: Post Data added");
        this.posts = posts;

        final DocumentReference docRef = firestore.collection("POSTS").document();
                docRef.set(posts)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!" + docRef.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });

    }

    public void addCommentToPosts( Posts posts1, Comments comments){
        Log.d(TAG, "addCommentToPosts: Comment added to post");
        this.posts = posts1;
        this.comments = comments;
        firestore.collection("COMMENTS").add(comments).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Log.d(TAG, "onSuccess: DocumentSnapshot successfully written" + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public void getCommentData(){
        Log.d(TAG, "getComment: called");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        if (document.exists()){
                            mCommentsFirestoreTask.commentDataAdded(Objects.requireNonNull(task.getResult()).toObjects(Comments.class));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }else {
                            mCommentsFirestoreTask.onError(task.getException());
                        }
                    }

                }
            }
        });
    }


//    Create interface to send data from repository to our ViewModel
    public interface OnFireStoreTaskComplete{
        void postDataAdded(List<Posts> postsList);
        void onError(Exception e);
    }
    public interface commentsFirestoreTask{
        void commentDataAdded(List<Comments> commentsList);
        void onError(Exception e);
    }



}
