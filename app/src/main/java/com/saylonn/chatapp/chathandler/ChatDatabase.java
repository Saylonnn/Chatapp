package com.saylonn.chatapp.chathandler;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/***
 * Android Room Datenbank zum lokalen Speichern der Nachrichten
 */
@Database(entities = {Chat.class, Message.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {
    public abstract ChatDao chatDao();
    public abstract MessageDao messageDao();
}
