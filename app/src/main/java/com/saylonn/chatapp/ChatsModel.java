package com.saylonn.chatapp;

public class ChatsModel {
    String friendName;
    int fId;
    int profilePicture;


    public ChatsModel(String friendName, int fId, int profilePicture) {
        this.friendName = friendName;
        this.fId = fId;
        this.profilePicture = profilePicture;
    }

    public String getFriendName() {
        return friendName;
    }

    public int getfId() {
        return fId;
    }

    public int getProfilePicture() {
        return profilePicture;
    }
}
