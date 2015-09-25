package com.br.fundamentosandroid.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.br.fundamentosandroid.models.entities.User;
import com.br.fundamentosandroid.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static class Singleton {
        public static final UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() {
        super();
    }

    public static UserRepository getInstance() {
        return Singleton.INSTANCE;
    }

    public static boolean isUser(String name, String password) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getReadableDatabase();

        String where = UserContract.LOGIN + " = ? AND " + UserContract.PASSWORD + " = ? ";
        String[] args = {name, password};
        String[] parameters = {UserContract.LOGIN, UserContract.PASSWORD};
        Cursor cursor = db.query(UserContract.TABLE, parameters, where, args, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            return true;
        }

        db.close();
        helper.close();

        return cursor.getCount() > 0;
    }

    public static void saveUser(User user) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(UserContract.TABLE, null, UserContract.getContentValues(user));
        db.close();
        helper.close();
    }

    public static List<User> getAll() {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(UserContract.TABLE, UserContract.COLUMNS, null, null, null, null, null);
        List<User> userList = UserContract.getUsers(cursor);
        db.close();
        helper.close();

        return userList;
    }

    public static User getUserByName(String name) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getReadableDatabase();
        String where = UserContract.LOGIN + " = ?";
        String[] args = {String.valueOf(name)};
        Cursor cursor = db.query(UserContract.TABLE, UserContract.COLUMNS, where, args, null, null, null);
        User user = UserContract.getUser(cursor);
        db.close();
        helper.close();
        return user;
    }

    public static User getUserById(int id) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getReadableDatabase();
        String where = UserContract.ID + " = ?";
        String[] args = {String.valueOf(id)};
        Cursor cursor = db.query(UserContract.TABLE, UserContract.COLUMNS, where, args, null, null, null);
        User user = UserContract.getUser(cursor);
        db.close();
        helper.close();
        return user;
    }

}
