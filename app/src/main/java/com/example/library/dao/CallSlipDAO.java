package com.example.library.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.library.ConnectionHelper;
import com.example.library.database.Sqldatabase;
import com.example.library.model.Book;
import com.example.library.model.CallSlip;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("maTV", callSlip.maTV);
//        contentValues.put("maSach", callSlip.maSach);
//        contentValues.put("ngay", callSlip.ngay);
//        contentValues.put("tienThue", callSlip.tienThue);
//        contentValues.put("traSach",callSlip.traSach);
//
//        return db.insert("CallSlip", null, contentValues);
        Connection connect;
        String ConnectionResult = "";
        boolean check = false;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "INSERT INTO ThongTinMuonTraSach (MaSachCP, TrangThai, NgayMuon, NgayTra, TienCoc, MaNguoiMuon) VALUES\n" +
                        "('CP016', 1, '29/01/2023', '9/02/2023', 18000, 'NM001')";
                Statement st = connect.createStatement();
                st.executeUpdate(sqlInsert);
                String query = "SELECT * FROM Sach";
                Statement st1 = connect.createStatement();
                ResultSet rs1 = st1.executeQuery(query);
                while (rs1.next())
                {
                    //book.maSach = rs1.getString(1);
                }
//                Log.v("View", "Insert book" + book.maSach);
//                for(int i = 0; i < book.SoluongCP; i++){
//                    String sqlInsertCP = "INSERT INTO BanSaoSach(ViTri, TrangThai, MaSach) VALUES\n" +
//                            "(N'"+book.ViTri+"', 0" + ",'" + book.maSach + "')";
//                    st.executeUpdate(sqlInsertCP);
//                }
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
    public int update(CallSlip callSlip)
    {
//        ContentValues contenValues = new ContentValues();
//        contenValues.put("ngay", callSlip.ngay);
//        contenValues.put("tienThue", callSlip.tienThue);
//        contenValues.put("traSach", callSlip.traSach);
//        return db.update ("CallSlip", contenValues, "maPH = ?", new String[]{String.valueOf(callSlip.maPH)});

        Connection connect;
        String ConnectionResult = "";
        boolean check = false;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
//                String sqlInsert = "UPDATE Sach\n" +
//                        "SET TenSach = N'" + book.tenSach + "', TenTacGia = N'" + book.tacGia + "', GiaThue = " + book.giaThue + ", MaNPH = " + Integer.parseInt(book.maLoai) + "\n" +
//                        "WHERE MaSach = '" + book.maSach + "';";
//                Statement st = connect.createStatement();
//                st.executeUpdate(sqlInsert);
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
//        List<CallSlip>list=new ArrayList<>();
//        Cursor cursor=db.rawQuery(sql,selectionArgs);
//        while (cursor.moveToNext()){
//            CallSlip callSlip =new CallSlip();
//            callSlip.maPH=Integer.parseInt(cursor.getString(cursor.getColumnIndex("maPH")));
//            callSlip.maTV = cursor.getString(cursor.getColumnIndex("maTV"));
//            callSlip.maSach = (cursor.getString(cursor.getColumnIndex("maSach")));
//            callSlip.ngay = cursor.getString(cursor.getColumnIndex("ngay"));
//            callSlip.tienThue=Integer.parseInt(cursor.getString(cursor.getColumnIndex("tienThue")));
//            callSlip.traSach=Integer.parseInt(cursor.getString(cursor.getColumnIndex("traSach")));
//            list.add(callSlip);
//        }
//        return list;
        List<CallSlip> list = new ArrayList<>();
        Connection connect;
        String ConnectionResult = "";
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null){
                String query = "SELECT * FROM ThongTinMuonTraSach";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                {
                    CallSlip callSlip = new CallSlip();
                    callSlip.maTT = rs.getString(1);
                    callSlip.maSach = rs.getString(2);
                    callSlip.traSach = Integer.parseInt(rs.getString(3));
                    callSlip.ngay = rs.getString(4);
                    callSlip.ngayTra = rs.getString(5);
                    callSlip.tienThue = rs.getInt(6);
                    callSlip.maTV = rs.getString(7);
                    list.add(callSlip);
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
