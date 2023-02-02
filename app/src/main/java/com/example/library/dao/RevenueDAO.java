package com.example.library.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.library.ConnectionHelper;
import com.example.library.database.Sqldatabase;
import com.example.library.model.Book;
import com.example.library.model.Top;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RevenueDAO {
    private SQLiteDatabase db;
    private Context context;
    SimpleDateFormat spd=new SimpleDateFormat("yyyy-MM-dd");

    public RevenueDAO(Context context) {
        this.context = context;
        Sqldatabase sqldatabase=new Sqldatabase(context);
        db=sqldatabase.getWritableDatabase();
    }

    public int getDoanhthu(String tuNgay,String denNgay){
//        String sqlDoanhthu ="Select sum(tienThue) as doanhThu From CallSlip Where ngay BetWeen ? and ?";
//        List<Integer> list=new ArrayList<Integer>();
//        Cursor cursor=db.rawQuery(sqlDoanhthu,new String[]{tuNgay,denNgay});
//        while (cursor.moveToNext()){
//            try {
//                list.add(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("doanhThu"))));
//
//            }catch (Exception e){
//                list.add(0);
//            }
//        }
//        return list.get(0);
        Connection connect;
        String ConnectionResult = "";
        int doanhThu = 0;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlDoanhThu = "SELECT \n" +
                        "\tSUM (ThongTinMuonTraSach.TienCoc) N'Doanh Thu Trong Tháng'\n" +
                        "FROM ThongTinMuonTraSach\n" +
                        "WHERE ThongTinMuonTraSach.NgayMuon >= '"+tuNgay+"'\n" +
                        "\tand ThongTinMuonTraSach.NgayMuon <= '"+denNgay+"'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(sqlDoanhThu);

                while (rs.next()){
                    doanhThu = rs.getInt(1);
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
        return doanhThu;
    }
    public List<Top> getTop(){
        String sqlTop ="Select maSach,count(maSach) as soLuong from CallSlip Group by maSach Order by soLuong desc limit 10";
        List<Top>list=new ArrayList<Top>();
        BookDAO bookDAO =new BookDAO(context);
        Cursor cursor=db.rawQuery(sqlTop,null);
        while (cursor.moveToNext()){
            Top top=new Top();

            Book book = bookDAO.getID(cursor.getString(cursor.getColumnIndexOrThrow("maSach")));
            top.tenSach= book.tenSach;
            top.soLuong=Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("soLuong")));
            list.add(top);
        }
        return list;
    }
}

