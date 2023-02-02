package com.example.library.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.library.ConnectionHelper;
import com.example.library.database.Sqldatabase;
import com.example.library.model.Member;
import com.example.library.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private SQLiteDatabase db;
    public MemberDAO(Context context){
        Sqldatabase sqldatabase=new Sqldatabase(context);
        db=sqldatabase.getWritableDatabase();

    }
    public long insert (Member member){
//        ContentValues values=new ContentValues();
//        values.put("hoTen", member.hoTen);
//        values.put("namSinh", member.namSinh);
//        return db.insert("Member",null,values);

        Connection connect;
        String ConnectionResult = "";
        boolean check = false;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "INSERT INTO NguoiMuon (MaTK, HoTen, MSSV, NamSinh) VALUES\n" +
                        "('" + member.maTK + "',N'" + member.hoTen + "'," + member.MSSV + ",' " + member.namSinh + "')";
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
    public int update(Member member){
        ContentValues contentValues=new ContentValues();
        contentValues.put("hoTen", member.hoTen);
        contentValues.put("namSinh", member.namSinh);
        return  db.update("Member",contentValues,"maTV=?",new String[]{String.valueOf(member.maTV)});

    }
    public int delete(String id){
        return db.delete("Member","maTV=?",new String[]{id});

    }
    public List<Member> getAll(){
        String sql="Select * from Member";
        return getData(sql);
    }
    public Member getID(String id){
        String sql="Select * from Member where maTV=?";
        List<Member> list=getData(sql,id);
        return list.get(0);
    }

    private List<Member>getData(String sql, String...selectionArgs){
//        List<Member>list =new ArrayList<>();
//        Cursor cursor=db.rawQuery(sql,selectionArgs);
//        while (cursor.moveToNext()){
//            Member member =new Member();
//            member.maTV=cursor.getString(cursor.getColumnIndexOrThrow("maTV"));
//            member.hoTen=cursor.getString(cursor.getColumnIndexOrThrow("hoTen"));
//            member.namSinh=cursor.getString(cursor.getColumnIndexOrThrow("namSinh"));
//            list.add(member);
//        }
//        return list;
        List<Member>list =new ArrayList<>();
        Connection connect;
        String ConnectionResult = "";
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null){
                String query = "SELECT * FROM NguoiMuon";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                {
                    Member member = new Member();
                    member.maTV = rs.getString(1);
                    member.maTK = rs.getString(2);
                    member.hoTen = rs.getString(3);
                    member.namSinh = rs.getString(5);
                    member.MSSV = Integer.parseInt(rs.getString(4));
                    list.add(member);
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
}
