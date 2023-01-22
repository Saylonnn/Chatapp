package com.saylonn.chatapp.chathandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Ein DAO wo die Datenbank Interaktionen für die Message Tabelle definiert werden
 * Man schreibt nur die Queries und Namen der Funktionen und diese werden
 * automatisch durch Android Room generiert
 */
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

    /**
     * Zum laden der Nachrichten die zu der jeweiligen Email gehören
     * @param chatEmail Email der Person mit der man schreibt
     * @return Gibt die Liste der zugehörigen Nachrichten
     */
    @Query("SELECT * FROM message WHERE chatEmail = :chatEmail ORDER BY id ASC")
    List<Message> getAllMessages(String chatEmail);

}