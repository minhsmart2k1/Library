package com.example.library.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

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
        String maSachCP = null;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {

                String sqlMaCP = "SELECT BanSaoSach.MaSachCP, BanSaoSach.MaSach, Sach.TenSach, BanSaoSach.TrangThai\n" +
                        "FROM BanSaoSach, Sach\n" +
                        "WHERE BanSaoSach.MaSach = Sach.MaSach\n" +
                        "\tand BanSaoSach.MaSach = '"+ callSlip.maSach +"'\n" +
                        "\tand BanSaoSach.TrangThai = 0";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(sqlMaCP);
//                if(!rs.first())
//                {
//                    Log.v("View", "Không đủ sách cho thuê!");
//                    return 0;
//                }
                while (rs.next())
                {
                    maSachCP = rs.getString(1);
                }

                String sqlInsert = "INSERT INTO ThongTinMuonTraSach (MaSachCP, TrangThai, NgayMuon, NgayTra, TienCoc, MaNguoiMuon) VALUES\n" +
                        "('"+maSachCP+"', "+callSlip.traSach+", '"+callSlip.ngay+"', '"+callSlip.ngayTra+"', "+callSlip.tienThue+", '"+callSlip.maTV+"')";

                st.executeUpdate(sqlInsert);
                String sqlUpdateSachCP = "UPDATE BanSaoSach\n" +
                        "SET TrangThai = 1\n" +
                        "WHERE MaSachCP = '"+maSachCP+"'";
                st.executeUpdate(sqlUpdateSachCP);
//                Log.v("View", "Insert book" + book.maSach);
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
        String maSachCP = null, maSach = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlCheck = "SELECT ThongTinMuonTraSach.MaTTMuonTra, ThongTinMuonTraSach.MaSachCP, Sach.MaSach\n" +
                        "FROM BanSaoSach, Sach, ThongTinMuonTraSach\n" +
                        "WHERE BanSaoSach.MaSach = Sach.MaSach\n" +
                        "\tand ThongTinMuonTraSach.MaSachCP = BanSaoSach.MaSachCP\n" +
                        "\tand ThongTinMuonTraSach.MaTTMuonTra = '"+callSlip.maPH+"'";

                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(sqlCheck);
                Log.v("Check update call slip", "Ma: " + callSlip.maPH);
                while (rs.next())
                {
                    maSachCP = rs.getString(2);
                    maSach = rs.getString(3);
                }
                if(!maSach.equals(callSlip.maSach)){
                    String sqlUpdateSachCP = "UPDATE BanSaoSach\n" +
                            "SET TrangThai = 0\n" +
                            "WHERE MaSachCP = '"+maSachCP+"'";
                    st.executeUpdate(sqlUpdateSachCP);
                    String sqlMaCP = "SELECT BanSaoSach.MaSachCP, BanSaoSach.MaSach, Sach.TenSach, BanSaoSach.TrangThai\n" +
                            "FROM BanSaoSach, Sach\n" +
                            "WHERE BanSaoSach.MaSach = Sach.MaSach\n" +
                            "\tand BanSaoSach.MaSach = '"+ callSlip.maSach +"'\n" +
                            "\tand BanSaoSach.TrangThai = 0";
                    Statement st1 = connect.createStatement();
                    ResultSet rs1 = st1.executeQuery(sqlMaCP);
                    while (rs1.next())
                    {
                        maSachCP = rs1.getString(1);
                    }
                    sqlUpdateSachCP = "UPDATE BanSaoSach\n" +
                            "SET TrangThai = 1\n" +
                            "WHERE MaSachCP = '"+maSachCP+"'";
                    st.executeUpdate(sqlUpdateSachCP);
                }
                String sqlUpdate = "UPDATE ThongTinMuonTraSach\n" +
                        "SET MaSachCP = '" +maSachCP+ "', TrangThai = "+callSlip.traSach+", NgayMuon = '"+callSlip.ngay +", NgayTra = '"+callSlip.ngayTra+"', TienCoc = "+callSlip.tienThue+", MaNguoiMuon = '"+callSlip.maTV+"'\n" +
                        "WHERE MaTTMuonTra = '"+callSlip.maPH+"'";
                st.executeUpdate(sqlUpdate);
                if(callSlip.traSach == 1){
                    String sqlUpdateSachCP = "UPDATE BanSaoSach\n" +
                            "SET TrangThai = 0\n" +
                            "WHERE MaSachCP = '"+maSachCP+"'";
                    st.executeUpdate(sqlUpdateSachCP);
                }else if(callSlip.traSach == 0){
                    String sqlUpdateSachCP = "UPDATE BanSaoSach\n" +
                            "SET TrangThai = 1\n" +
                            "WHERE MaSachCP = '"+maSachCP+"'";
                    st.executeUpdate(sqlUpdateSachCP);
                }
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
        //return db.delete("CallSlip", "maPH = ?", new String[]{id});
        Connection connect;
        String ConnectionResult = "";

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlDelete = "DELETE FROM ThongTinMuonTraSach\n" +
                        "WHERE MaTTMuonTra = '" + id + "';";
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
                    callSlip.maPH = rs.getString(1);
                    callSlip.maSach = rs.getString(2);
                    callSlip.traSach = Integer.parseInt(rs.getString(3));
                    callSlip.ngay = rs.getString(4);
                    callSlip.ngayTra = rs.getString(5);
                    callSlip.tienThue = rs.getInt(6);
                    callSlip.maTV = rs.getString(7);
                    list.add(callSlip);
                }
                Log.v("Size Call Slip", "Size: " + list.size());
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
