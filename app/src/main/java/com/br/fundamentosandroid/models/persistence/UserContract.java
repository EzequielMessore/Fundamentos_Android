package com.br.fundamentosandroid.models.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import com.br.fundamentosandroid.models.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserContract {

    public static final String TABLE = "users";
    public static final String ID = "id";
    public static final String LOGIN = "name";
    public static final String PASSWORD = "password";

    public static final String[] COLUMNS = {ID, LOGIN,PASSWORD};

    public static String createTable(){
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ")
                .append(TABLE+" (")
                .append(ID + " INTEGER PRIMARY KEY, ")
                .append(LOGIN + " TEXT,")
                .append(PASSWORD + " TEXT")
                .append(");");
        return sql.toString();
    }

    public static ContentValues getContentValues(User user) {
        ContentValues content = new ContentValues();
        content.put(ID, user.getId());
        content.put(LOGIN,user.getName());
        content.put(PASSWORD,user.getPassword());
        return content;
    }

    public static User getUser(Cursor cursor) {
        if (!cursor.isBeforeFirst() || cursor.moveToNext()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(LOGIN)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            return user;
        }
        return null;
    }

    public static List<User> getUsers(Cursor cursor) {
        final List<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            users.add(getUser(cursor));
        }
        return users;
    }


}
