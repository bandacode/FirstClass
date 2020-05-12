package com.example.anchat.ui.posts;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anchat.data.model.Comments;
import com.example.anchat.data.model.Posts;
import com.example.anchat.data.repository.PostsRepo;

import java.util.List;

public class PostViewModel extends ViewModel implements PostsRepo.OnFireStoreTaskComplete, PostsRepo.commentsFirestoreTask {

    private static final String TAG = "PostViewModel";

    private PostsRepo postsRepo = new PostsRepo((PostsRepo.OnFireStoreTaskComplete) this);
    private PostsRepo mPostsRepo = new PostsRepo((PostsRepo.commentsFirestoreTask) this);

    private MutableLiveData<List<Posts>> postsListModelData = new MutableLiveData<>();
    private MutableLiveData<List<Comments>> commentListModelData = new MutableLiveData<>();

    public LiveData<List<Posts>> getPostsListModelData() {
        if (postsListModelData == null){
            postsListModelData = new MutableLiveData<>();
        }
        return postsListModelData;
    }

    public LiveData<List<Comments>> getCommentsListModelData(){
        if (commentListModelData == null){
            commentListModelData = new MutableLiveData<>();
        }
        return commentListModelData;
    }


    public PostViewModel(){
        Log.d(TAG, "PostViewModel: initialised");
        postsRepo.getPostData();
        mPostsRepo.getCommentData();
    }



    @Override
    public void postDataAdded(List<Posts>arrayList) {
        postsListModelData.setValue(arrayList);


    }

    @Override
    public void commentDataAdded(List<Comments> commentsList) {
        commentListModelData.setValue(commentsList);


    }

    @Override
    public void onError(Exception e) {

    }
}