package com.example.library.model;

public class CallSlip {
    public String maPH;
    public String maTT;
    public String maTV;
    public String maSach;
    public String ngay;
    public String ngayTra;
    public int tienThue;
    public int traSach;
    public CallSlip(String maPH, String maTT, String maTV, String maSach, String ngay, String ngayTra, int tienThue, int traSach)
    {
        this.maPH = maPH;
        this.maTT = maTT;
        this.maTV = maTV;
        this.maSach = maSach;
        this.ngay = ngay;
        this.ngayTra = ngayTra;
        this.tienThue = tienThue;
        this.traSach = traSach;
    }
    public CallSlip(){

    }
}
