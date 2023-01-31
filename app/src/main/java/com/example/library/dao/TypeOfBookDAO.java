package com.example.library.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.library.ConnectionHelper;
import com.example.library.database.Sqldatabase;
import com.example.library.model.Book;
import com.example.library.model.TypeOfBook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TypeOfBookDAO {
    private SQLiteDatabase db;
    public TypeOfBookDAO(Context context){
        Sqldatabase sqldatabase=new Sqldatabase(context);
        db=sqldatabase.getWritableDatabase();
    }
    public long insert(TypeOfBook TypeOfBook){
        /*
        ContentValues contentValues=new ContentValues();
        contentValues.put("tenLoai", TypeOfBook.tenLoai);
        contentValues.put("nCC", TypeOfBook.nCC);
        return db.insert("TypeOfBook",null,contentValues);

         */
        Connection connect;
        String ConnectionResult = "";
        boolean check = false;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "INSERT INTO NhaPhatHanh VALUES (N'" + TypeOfBook.tenLoai + "')";
                Statement st = connect.createStatement();
                st.executeUpdate(sqlInsert);
                check = true;
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex){
            Log.e("Error", ex.getMessage());
        }
        if(check)
            return 1;
        return 0;
    }
    public int update(TypeOfBook TypeOfBook){
        /*
        ContentValues contentValues=new ContentValues();
        contentValues.put("tenLoai", TypeOfBook.tenLoai);
        contentValues.put("nCC", TypeOfBook.nCC);
        return db.update("TypeOfBook",contentValues,"maLoai=?",new String[]{String.valueOf(TypeOfBook.maLoai)});

         */

        Connection connect;
        String ConnectionResult = "";
        boolean check = false;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "UPDATE NhaPhatHanh\n" +
                        "SET TenNPH = N'" + TypeOfBook.tenLoai + "'\n" +
                        "WHERE MaNPH = " + Integer.parseInt(TypeOfBook.maLoai) + ";";
                Statement st = connect.createStatement();
                st.executeUpdate(sqlInsert);
                check = true;
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex){
            Log.e("Error", ex.getMessage());
        }
        if(check)
            return 1;
        return 0;
    }
    public int delete(String id){
        /*
        return
                db.delete("TypeOfBook","maLoai=?",new String[]{id});

         */

        Connection connect;
        String ConnectionResult = "";

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlDelete = "DELETE FROM NhaPhatHanh\n" +
                        "WHERE MaNPH = '" + id + "';";
                Statement st = connect.createStatement();
                st.executeUpdate(sqlDelete);
                return 1;
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex){
            Log.e("Error", ex.getMessage());
        }
        return 0;
    }
    public List<TypeOfBook> getAll(){
        String sql="Select * from TypeOfBook";
        return getData(sql);
    }
    public TypeOfBook getID(String id){
        String sql="select * from TypeOfBook where maLoai=?";
        List<TypeOfBook>list=getData(sql,id);
        return list.get(0);

    }

    private List<TypeOfBook> getData(String sql, String...selectionArgs){
//        List<TypeOfBook>list =new ArrayList<>();
//        Cursor cursor=db.rawQuery(sql,selectionArgs);
//        while (cursor.moveToNext()){
//            TypeOfBook TypeOfBook =new TypeOfBook();
//            TypeOfBook.maLoai=Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("maLoai")));
//            TypeOfBook.tenLoai=cursor.getString(cursor.getColumnIndexOrThrow("tenLoai"));
//            TypeOfBook.nCC = cursor.getString(cursor.getColumnIndexOrThrow("nCC"));
//            list.add(TypeOfBook);
//        }
//        return list;
        List<TypeOfBook>list =new ArrayList<>();
        Connection connect;
        String ConnectionResult = "";
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null){
                String query = "Select * from NhaPhatHanh";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                {
                    TypeOfBook typeOfBook = new TypeOfBook();
                    typeOfBook.maLoai = rs.getString(1);
                    typeOfBook.tenLoai = rs.getString(2);
                    typeOfBook.nCC = "Tham khảo";
                    list.add(typeOfBook);
                }
            }
            else
            {
                ConnectionResult = "Check Connection";
            }
        }
        catch (Exception ex){
            Log.e("Error", ex.getMessage());
        }
        return list;
    }


}
