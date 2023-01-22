package com.saylonn.chatapp.chathandler;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/***
 * Chat Tabelle in der DB
 */
@Entity(indices = {@Index(value = {"email"},unique = true)})
public class Chat implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;

    private String email;

    private String last_msg;

    public Chat(String username, String email, String last_msg) {
        this.username = username;
        this.email = email;
        this.last_msg = last_msg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getLast_msg() {
        return last_msg;
    }
}
