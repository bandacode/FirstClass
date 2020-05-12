package com.example.anchat.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.firestore.v1.DocumentTransform;

import java.util.Calendar;
import java.util.Date;

public class Posts implements Parcelable {


    private String postID;
    private String groupID;
    private Users postAuthor;
    private String postTitle;
    private String postBody;
    @ServerTimestamp
    private Date timestamp;
    private long commentsCount;
    private String postImageURL;

    public Posts(Users postAuthor, String postTitle, String postBody, String postImageURL) {
        this.postAuthor = postAuthor;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.commentsCount  = 0;
        this.postImageURL = postImageURL;
        this.timestamp = null;

    }
    public Posts(Users postAuthor, String postTitle, String postBody, String postImageURL, String groupId) {
        this.postAuthor = postAuthor;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.commentsCount  = 0;
        this.postImageURL = postImageURL;
        this.groupID = groupId;
        this.timestamp = null;

    }

    public Posts (String postTitle, String postBody, String postImageURL ){
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postImageURL = postImageURL;

    }

    public Posts() {
    }

    public Posts(Users user, Object o, String string) {

    }

    public Posts(Users postAuthor, String postID, String postText, String postBodyText, String postImage, String groupID) {
        this.postAuthor = postAuthor;
        this.postTitle = postText;
        this.postBody = postBodyText;
        this.commentsCount  = 0;
        this.postImageURL = postImageURL;
        this.groupID = groupID;
        this.timestamp = null;

    }


    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Users getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(Users postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public long getNumberOfComments() {
        return commentsCount;
    }

    public void setNumberOfComments(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostImageURL() {
        return postImageURL;
    }

    public void setPostImageURL(String postImageURL) {
        this.postImageURL = postImageURL;
    }



    protected Posts(Parcel in) {
    }

    public static final Creator<Posts> CREATOR = new Creator<Posts>() {
        @Override
        public Posts createFromParcel(Parcel in) {
            return new Posts(in);
        }

        @Override
        public Posts[] newArray(int size) {
            return new Posts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
