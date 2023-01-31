package com.example.library.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.library.database.Sqldatabase;
import com.example.library.model.CallSlip;

import java.util.ArrayList;
import java.util.List;

public class CallSlipDAO {
    private SQLiteDatabase db;
    public CallSlipDAO(Context context)
    {
        Sqldatabase sqLiteDatabase = new Sqldatabase(context);
        db = sqLiteDatabase.getWritableDatabase();
    }
    public long insert(CallSlip callSlip){
        ContentValues contentValues = new ContentValues();
        contentValues.put("maTV", callSlip.maTV);
        contentValues.put("maSach", callSlip.maSach);
        contentValues.put("ngay", callSlip.ngay);
        contentValues.put("tienThue", callSlip.tienThue);
        contentValues.put("traSach",callSlip.traSach);

        return db.insert("CallSlip", null, contentValues);

    }
    public int update(CallSlip callSlip)
    {
        ContentValues contenValues = new ContentValues();
        contenValues.put("ngay", callSlip.ngay);
        contenValues.put("tienThue", callSlip.tienThue);
        contenValues.put("traSach", callSlip.traSach);
        return db.update ("CallSlip", contenValues, "maPH = ?", new String[]{String.valueOf(callSlip.maPH)});
    }
    public int delete(String id)
    {
        return db.delete("CallSlip", "maPH = ?", new String[]{id});
    }
    public List<CallSlip> getAll(){
        String sql = "select *from CallSlip";
        return getData(sql);
    }

    public CallSlip getID(String id){
        String sql="select*from CallSlip Where maPH=?";
        List<CallSlip>list=getData(sql,id);
        return list.get(0);
    }
    @SuppressLint("Range")
    private List<CallSlip> getData(String sql, String...selectionArgs){
        List<CallSlip>list=new ArrayList<>();
        Cursor cursor=db.rawQuery(sql,selectionArgs);
        while (cursor.moveToNext()){
            CallSlip callSlip =new CallSlip();
            callSlip.maPH=Integer.parseInt(cursor.getString(cursor.getColumnIndex("maPH")));
            callSlip.maTV = cursor.getString(cursor.getColumnIndex("maTV"));
            callSlip.maSach = (cursor.getString(cursor.getColumnIndex("maSach")));
            callSlip.ngay = cursor.getString(cursor.getColumnIndex("ngay"));
            callSlip.tienThue=Integer.parseInt(cursor.getString(cursor.getColumnIndex("tienThue")));
            callSlip.traSach=Integer.parseInt(cursor.getString(cursor.getColumnIndex("traSach")));
            list.add(callSlip);
        }
        return list;
    }
}
