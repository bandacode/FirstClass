package com.example.anchat.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

public class Groups implements Parcelable {

    @DocumentId
    public String groupId;
    public String userId;
    public String groupName;
    public String groupDescription;
    public String groupImageUrl;
    public Posts latestPost;

    public Groups() {
    }

    public Groups(String groupName, String groupDescription, String groupImageUrl, String userId) {
        this.userId = userId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupImageUrl = groupImageUrl;
    }

    protected Groups(Parcel in) {
        groupId = in.readString();
        groupName = in.readString();
        groupDescription = in.readString();
        groupImageUrl = in.readString();
        latestPost = in.readParcelable(Posts.class.getClassLoader());
    }

    public Groups(String title, String desc) {
//        Just for now, will change after we test it to take up all other details of the model
        this.groupName = title;
        this.groupDescription = desc;
    }

    public Groups(String groupID, String groupName, String desc, String image, String userID) {
        this.groupId = groupID;
        this.groupName = groupName;
        this.groupDescription = desc;
        this.groupImageUrl = image;
        this.userId = userID;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeString(groupDescription);
        dest.writeString(groupImageUrl);
        dest.writeParcelable(latestPost, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Groups> CREATOR = new Creator<Groups>() {
        @Override
        public Groups createFromParcel(Parcel in) {
            return new Groups(in);
        }

        @Override
        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Posts getLatestPost() {
        return latestPost;
    }

    public void setLatestPost(Posts latestPost) {
        this.latestPost = latestPost;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }
    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }
}
