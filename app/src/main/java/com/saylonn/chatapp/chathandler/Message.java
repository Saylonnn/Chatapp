package com.saylonn.chatapp.chathandler;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = {@ForeignKey(entity = Chat.class,
        parentColumns = "email",
        childColumns = "chatEmail"
)})
public class Message implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sender;

    private String message;

    private String chatEmail;

    public Message(String sender, String chatEmail, String message) {
        this.sender = sender;
        this.chatEmail = chatEmail;
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setChatEmail(String chatEmail) {
        this.chatEmail = chatEmail;
    }

    public String getChatEmail() {
        return chatEmail;
    }

}