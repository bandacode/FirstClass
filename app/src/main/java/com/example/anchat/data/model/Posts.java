package com.example.anchat.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Posts implements Parcelable {

    @DocumentId
    private String postID;
    private String groupId;
    private Users postAuthor;
    private String postTitle;
    private String postBody;
    @ServerTimestamp
    private Date timestamp;
    private long commentsCount;
    private String postImageURL;

    public Posts(Users postAuthor, String postTitle, String postBody, String groupId) {
        this.postAuthor = postAuthor;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.commentsCount  = 0;
        this.groupId = groupId;
        this.timestamp = null;
    }

    public Posts(Users postAuthor, String postTitle, String postBody, String postImageURL, String groupId) {
        this.postAuthor = postAuthor;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.commentsCount  = 0;
        this.postImageURL = postImageURL;
        this.groupId = groupId;
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

    public String getPostID() {
        return postID;
    }

    public void setPostID(String groupId) {
        this.postID = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
