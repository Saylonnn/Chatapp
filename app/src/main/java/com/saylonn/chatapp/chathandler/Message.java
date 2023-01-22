package com.saylonn.chatapp.chathandler;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/**
 * Message Tabelle wo die gesendeten und empfangenen Nachrichten gespiechert werden
 * chatEmail is ein Foreign Key welcher die email aus der Chat Tabelle referenziert
 */
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

    /**
     *  Um eine Nachricht in die Tabelle hinzufügen zu können
     *
     * @param sender Wer die Nachricht gesendet hat, wird genutzt um unterschiedliche layout Datei zu nutzen
     *               (Wenn man sendet wird die Nachricht rechts und wenn man eine empfängt links angezeigt)
     * @param chatEmail Damit die richtigen Nachrichten geladen werden wenn man auf den Chat klickt
     * @param message Die gesendete/empfangene Nachricht
     */
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