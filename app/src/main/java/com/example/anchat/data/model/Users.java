package com.example.anchat.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

public class Users implements Parcelable {
    public String userID;
    public String profileName;

    public String pictureUrl;
    public String emailAddress;
    public String aboutUser;

    public Users (){}

    public Users(String userID, String profileName, String pictureUrl, String emailAddress, String aboutUser) {
        this.userID = userID;
        this.profileName = profileName;
        this.pictureUrl = pictureUrl;
        this.emailAddress = emailAddress;
        this.aboutUser = aboutUser;
    }

    public Users(String userID, String profileName, String pictureUrl, String emailAddress) {
        this.userID = userID;
        this.profileName = profileName;
        this.pictureUrl = pictureUrl;
        this.emailAddress = emailAddress;
    }

    protected Users(Parcel in) {
        userID = in.readString();
        profileName = in.readString();
        pictureUrl = in.readString();
        emailAddress = in.readString();

    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(String aboutUser) {
        this.aboutUser = aboutUser;
    }


    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Users(FirebaseUser firebaseUser) {
        this.userID = firebaseUser.getUid();
        this.profileName = firebaseUser.getDisplayName();
        if (firebaseUser.getPhotoUrl() != null) {
            this.pictureUrl = firebaseUser.getPhotoUrl().toString();
        }
        this.emailAddress = firebaseUser.getEmail();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(profileName);
        parcel.writeString(pictureUrl);
        parcel.writeString(emailAddress);

    }
}
