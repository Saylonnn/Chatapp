package com.saylonn.chatapp.chathandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Ein DAO wo die Datenbank Interaktionen für die Chat Tabelle definiert werden
 * Man schreibt nur die Queries und Namen der Funktionen und diese werden
 * automatisch durch Android Room generiert
 */
@Dao
public interface ChatDao {

    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);

    @Query("DELETE FROM chat")
    void deleteAllChats();

    @Query("SELECT * FROM chat ORDER BY id ASC")
    List<Chat> getAllChats();

    @Query("SELECT EXISTS(SELECT * FROM chat WHERE email = :email)")
    Boolean isRowIsExist(String email);

}
