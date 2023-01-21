package com.saylonn.chatapp.chathandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM chat")
    void deleteAllMessages();

    @Query("SELECT * FROM message WHERE chatEmail = :chatEmail ORDER BY id ASC")
    List<Message> getAllMessages(String chatEmail);

}