package com.example.library.model;

public class Book {
    public String maSach;
    public String tenSach;
    public int giaThue;
    public String maLoai;
    public int giamGia;
    public String tacGia;
    public int SoluongCP;
    public String ViTri;

    public Book(String maSach, String tenSach, int giaThue, String maLoai, int giamGia, String tacGia, int SoluongCP, String ViTri) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.giaThue = giaThue;
        this.maLoai = maLoai;
        this.giamGia = giamGia;
        this.tacGia = tacGia;
        this.SoluongCP = SoluongCP;
        this.ViTri = ViTri;
    }

    public Book() {
    }
}