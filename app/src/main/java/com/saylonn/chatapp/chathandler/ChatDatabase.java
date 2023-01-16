package com.saylonn.chatapp.chathandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ChatDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ChatHistory.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Chat1";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SENDER = "sender";
    private static final String COLUMN_MESSAGE = "message";

    public ChatDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                "CREATE TABLE " + TABLE_NAME
                        + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_SENDER + " TEXT, "
                        + COLUMN_MESSAGE + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addLocalMessage(String sender, String message) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SENDER, sender);
        contentValues.put(COLUMN_MESSAGE, message);

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readMyData() {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SENDER + " LIKE 'localUser'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;

        if(sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readReceivedData() {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SENDER + " NOT LIKE 'localUser'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;

        if(sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }
}
