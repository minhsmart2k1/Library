package com.example.library.model;

public class Member {
    public String maTV;
    public String maTK;
    public String hoTen;
    public int MSSV;
    public String namSinh;

    public Member(String maTV, String maTK, String hoTen, int MSSV, String namSinh) {
        this.maTV = maTV;
        this.maTK = maTK;
        this.hoTen = hoTen;
        this.MSSV = MSSV;
        this.namSinh = namSinh;
    }

    public Member() {
    }
}
