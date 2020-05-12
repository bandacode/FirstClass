package com.example.anchat.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

public class Comments implements Parcelable {
    private String commentText;
    private Date commentTimestamp;
    private String commentID;
    private Users commentsAuthor;
    private String postID;

    public Comments() {
    }

    public Comments(String commentText, Users commentsAuthor){
        this.commentText = commentText;
        this.commentID = UUID.randomUUID().toString();
        this.commentsAuthor = commentsAuthor;
        this.commentTimestamp = null;
    }

    public Comments(String commentText, String commentID, Users commentsAuthor, String postId) {
        this.commentText = commentText;
        this.commentID = commentID;
        this.commentsAuthor = commentsAuthor;
        this.postID = postId;
    }

    protected Comments(Parcel in) {
        commentText = in.readString();
//        commentTimestamp = in.readLong();
        commentID = in.readString();
        commentsAuthor = in.readParcelable(Users.class.getClassLoader());
    }

    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel in) {
            return new Comments(in);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };

    public Comments(String commentText, Object commentTimestamp, Users users, String postId) {
        this.commentText = commentText;
        this.commentTimestamp = (Date) commentTimestamp;
        this.postID = postId;
        this.commentsAuthor = users;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(commentText);
        parcel.writeString(commentID);
        parcel.writeParcelable(commentsAuthor, i);
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentTimestamp() {
        return commentTimestamp;
    }

    public void setCommentTimestamp(Date commentTimestamp) {
        this.commentTimestamp = commentTimestamp;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public Users getCommentsAuthor() {
        return commentsAuthor;
    }

    public void setCommentsAuthor(Users commentsAuthor) {
        this.commentsAuthor = commentsAuthor;
    }

    public String getPostID() {
        return postID;
    }

    public String setPostID(String postID) {
        this.postID = postID;
        return postID;
    }

}
