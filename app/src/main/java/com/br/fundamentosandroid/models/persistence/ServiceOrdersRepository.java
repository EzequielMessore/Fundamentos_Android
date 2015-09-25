package com.br.fundamentosandroid.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.br.fundamentosandroid.models.entities.ServiceOrder;
import com.br.fundamentosandroid.models.entities.User;
import com.br.fundamentosandroid.util.AppUtil;
import com.br.fundamentosandroid.util.SharedPreferenceUtil;

import java.util.List;

public final class ServiceOrdersRepository {

    private ServiceOrdersRepository() {
        super();
    }

    public static void saveOrUpdate(ServiceOrder serviceOrder) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getWritableDatabase();
        serviceOrder.setActive(true);
        if (serviceOrder.getId() == null) {
            db.insert(ServiceOrderContract.TABLE, null, ServiceOrderContract.getContentValues(serviceOrder));
        } else {
            String where = ServiceOrderContract.ID + " = ?";
            String[] args = {serviceOrder.getId().toString()};
            db.update(ServiceOrderContract.TABLE, ServiceOrderContract.getContentValues(serviceOrder), where, args);
        }
        db.close();
        helper.close();
    }

    public static void disable(ServiceOrder serviceOrder) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getWritableDatabase();

        String where = ServiceOrderContract.ID + " = ?";
        String[] args = {serviceOrder.getId().toString()};
        serviceOrder.setActive(false);
        db.update(ServiceOrderContract.TABLE, ServiceOrderContract.getContentValues(serviceOrder), where, args);

        db.close();
        helper.close();
    }

    public static void isActive(ServiceOrder serviceOrder){
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getWritableDatabase();
        
        String where = ServiceOrderContract.ID + " = ?";
        String[] args = {serviceOrder.getId().toString()};
        serviceOrder.setActive(true);
        db.update(ServiceOrderContract.TABLE, ServiceOrderContract.getContentValues(serviceOrder), where, args);
        
        db.close();
        helper.close();
    }

    public static List<ServiceOrder> getAll(boolean archived) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getReadableDatabase();

        String where = ServiceOrderContract.ACTIVE + " = ?" + " AND " + ServiceOrderContract.USER_ID + " = ? ";

        String[] args = new String[2];
        args[0] = "1";
        if(archived){
            args[0] = "0";
        }
        //TODO
        User user = SharedPreferenceUtil.getUserPreference();
        args[1] = String.valueOf(user.getId());

        Cursor cursor = db.query(ServiceOrderContract.TABLE, ServiceOrderContract.COLUMNS, where, args, null, null, ServiceOrderContract.DATE);
        List<ServiceOrder> serviceOrders = ServiceOrderContract.bindList(cursor);
        db.close();
        helper.close();
        return serviceOrders;
    }

    public static void delete(Integer id) {
        DataBaseHelper helper = new DataBaseHelper(AppUtil.CONTEXT_APPLICATION);
        SQLiteDatabase db = helper.getWritableDatabase();

        String where = ServiceOrderContract.ID + " = ?";
        String[] args = {String.valueOf(id)};
        db.delete(ServiceOrderContract.TABLE, where, args);

        db.close();
        helper.close();
    }

}