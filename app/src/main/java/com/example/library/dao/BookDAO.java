package com.example.library.dao;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
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

public class BookDAO {
    private SQLiteDatabase db;

    public BookDAO(Context context) {
        Sqldatabase sqldatabase = new Sqldatabase(context);
        db = sqldatabase.getWritableDatabase();
    }
    public long insert(Book book) {
        /*
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenSach", book.tenSach);
        contentValues.put("giaThue", book.giaThue);
        contentValues.put("maLoai", book.maLoai);
        contentValues.put("giamGia", book.giamGia);
        contentValues.put("tacGia", book.tacGia);
        return db.insert("Book", null, contentValues);

         */
        Connection connect;
        String ConnectionResult = "";
        boolean check = false;

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlInsert = "INSERT INTO Sach (TenSach, TenTacGia,GiaThue,ViTri, SoLuongCP, MaNPH) " +
                        "VALUES (N'" + book.tenSach + "', N'" + book.tacGia + "'," + book.giaThue + ",N'" +book.ViTri+"'," + book.SoluongCP + "," + book.maLoai + ")";
                Statement st = connect.createStatement();
                st.executeUpdate(sqlInsert);
                String query = "SELECT * FROM Sach";
                Statement st1 = connect.createStatement();
                ResultSet rs1 = st1.executeQuery(query);
                while (rs1.next())
                {
                    book.maSach = rs1.getString(1);
                }
                Log.v("View", "Insert book" + book.maSach);
                for(int i = 0; i < book.SoluongCP; i++){
                    String sqlInsertCP = "INSERT INTO BanSaoSach(ViTri, TrangThai, MaSach) VALUES\n" +
                            "(N'"+book.ViTri+"', 0" + ",'" + book.maSach + "')";
                    st.executeUpdate(sqlInsertCP);
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
    public int update(Book book) {
        /*
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenSach", book.tenSach);
        contentValues.put("giaThue", book.giaThue);
        contentValues.put("maLoai", book.maLoai);
        contentValues.put("giamGia", book.giamGia);
        contentValues.put("tacGia", book.tacGia);
        return db.update("Book", contentValues, "maSach=?",new String[]{String.valueOf(book.maSach)});

         */
        Connection connect;
        String ConnectionResult = "";
        boolean check = false;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlCheck = "SELECT * FROM Sach WHERE MaSach = '" + book.maSach + "'";
                int sl = 0;
                Statement st1 = connect.createStatement();
                ResultSet rs1 = st1.executeQuery(sqlCheck);
                while (rs1.next()){
                    sl = rs1.getInt(7);
                }
                if(sl == book.SoluongCP) {
                    String sqlInsert = "UPDATE Sach\n" +
                            "SET TenSach = N'" + book.tenSach + "', TenTacGia = N'" + book.tacGia + ",ViTri = N'" +book.ViTri+"', SoLuongCP" + book.SoluongCP + "', GiaThue = " + book.giaThue + ", MaNPH = " + Integer.parseInt(book.maLoai) + "\n" +
                            "WHERE MaSach = '" + book.maSach + "'";
                    Statement st = connect.createStatement();
                    st.executeUpdate(sqlInsert);
                }
                else{
                    if(sl < book.SoluongCP){
                        String sqlInsert = "UPDATE Sach\n" +
                                "SET TenSach = N'" + book.tenSach + "', TenTacGia = N'" + book.tacGia + "',ViTri = N'" +book.ViTri+"', SoLuongCP = " + book.SoluongCP + ", GiaThue = " + book.giaThue + ", MaNPH = " + Integer.parseInt(book.maLoai) + "\n" +
                                "WHERE MaSach = '" + book.maSach + "'";
                        Statement st = connect.createStatement();
                        st.executeUpdate(sqlInsert);
                        for(int i = 0; i < (book.SoluongCP - sl); i++) {
                            String sqlInsertCP = "INSERT INTO BanSaoSach(ViTri, TrangThai, MaSach) VALUES\n" +
                                    "(N'" + book.ViTri + "', 0" + ",'" + book.maSach + "')";
                            st1.executeUpdate(sqlInsertCP);
                        }
                    }
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
    public int delete(String id) {
        /*
        return
                db.delete("Book", "maSach=?", new String[]{id});

         */
        Connection connect;
        String ConnectionResult = "";

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null) {
                String sqlDelete = "DELETE FROM Sach\n" +
                        "WHERE MaSach = '" + id + "';";
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
    public List<Book> getAll() {
        String sql = "select * from Book";
        return getData(sql);
    }
    public Book getID(String id) {
        String sql = "select * from Book where maSach=?";
        List<Book> list2 = new ArrayList<>();
        list2 = getData(sql, id);
        return list2.get(0);

    }

    private List<Book> getData(String sql, String... selectionArgs) {
//        List<Book> list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(sql, selectionArgs);
//
//        while (cursor.moveToNext()) {
//            Book book = new Book();
//            book.maSach = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("maSach")));
//            book.maLoai = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("maLoai")));
//            book.tenSach = cursor.getString(cursor.getColumnIndexOrThrow("tenSach"));
//            book.giaThue = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("giaThue")));
//            book.giamGia = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("giamGia")));
//            book.tacGia = cursor.getString(cursor.getColumnIndexOrThrow("tacGia"));
//            list.add(book);
//        }
//        return list;

        List<Book> list = new ArrayList<>();
        Connection connect;
        String ConnectionResult = "";
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.connectionclass();
            if(connect != null){
                String query = "SELECT * FROM Sach";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                {
                    Book book = new Book();
                    book.maSach = rs.getString(1);
                    book.maLoai = rs.getString(8);
                    book.tenSach = rs.getString(2);
                    book.giaThue = rs.getInt(4);
                    book.giamGia = 1000;
                    book.tacGia = rs.getString(3);
                    book.ViTri = rs.getString(6);
                    book.SoluongCP = Integer.parseInt(rs.getString(7));
                    list.add(book);
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
