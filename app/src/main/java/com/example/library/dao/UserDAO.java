package com.example.library.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.library.ConnectionHelper;
import com.example.library.database.Sqldatabase;
import com.example.library.model.TypeOfBook;
import com.example.library.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    SQLiteDatabase db;
    public UserDAO(Context context){
        Sqldatabase sqldatabase=new Sqldatabase(context);
        db=sqldatabase.getWritableDatabase();
    }
    public long insert(User user){
        /*
        ContentValues contentValues=new ContentValues();
        contentValues.put("maTT",user.maTT);
        contentValues.put("hoTen",user.hoTen);
        contentValues.put("matKhau",user.matKhau);
        return db.insert("User",null,contentValues);

         */
        Connection connect;
        String ConnectionResult = "";
        boolean check = false;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "INSERT INTO TaiKhoan VALUES ('" + user.maTT + "', '" + user.matKhau + "', N'" +
                        user.hoTen + "', N'Nam', N'0948918430', 'mn113839@gmail.com')";
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
    public int update(User user){
        /*
        ContentValues contentValues=new ContentValues();
        contentValues.put("maTT",user.maTT);
        contentValues.put("hoTen",user.hoTen);
        contentValues.put("matKhau",user.matKhau);
        return db.update("User",contentValues,"maTT=?",new String[]{String.valueOf(user.maTT)});

         */
        Connection connect;
        String ConnectionResult = "";
        boolean check = false;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "UPDATE TaiKhoan\n" +
                        "SET MatKhau = '" + user.matKhau + "', HoTen = N'" + user.hoTen + "\n" +
                        "WHERE MaTK = '" + user.maTT + "';";
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
    public  int delete(String id){
        return
                db.delete("User","maTT=?",new String[]{id});
    }
    public List<User>getAll(){
        String sql="Select *from User";
        return getData(sql);
    }
    public  User getID (String id){
        String sql="select * from User where maTT=?";
        List<User> list=getData(sql,id);
        return list.get(0);
    }
    @SuppressLint("Range")
    private List<User>getData(String sql, String ...selectionArgs){
//        List<User>list=new ArrayList<>();
//        Cursor cursor=db.rawQuery(sql,selectionArgs);
//        while (cursor.moveToNext()){
//            User user=new User();
//            user.maTT=cursor.getString(cursor.getColumnIndex("maTT"));
//            user.hoTen=cursor.getString(cursor.getColumnIndex("hoTen"));
//            user.matKhau=cursor.getString(cursor.getColumnIndex("matKhau"));
//            list.add(user);
//        }
//        return list;

        List<User>list =new ArrayList<>();
        Connection connect;
        String ConnectionResult = "";
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null){
                String query = "Select * from TaiKhoan";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                {
                    User user = new User();
                    user.maTT = rs.getString(1);
                    user.matKhau = rs.getString(2);
                    user.hoTen = rs.getString(3);
                    list.add(user);
                }
                Log.v("Size User", "Size: " + list.size());
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
    public  int checkLogin(String id,String password){
        String sql="select * from User where maTT=? and matKhau=?";
        List<User>list=getData(sql,id,password);
        if (list.size()==0){
            return -1;
        }
        return 1;
    }
}
