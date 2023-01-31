package com.example.library.model;

public class CallSlip {
    public int maPH;
    public String maTT;
    public String maTV;
    public String maSach;
    public String ngay;
    public int tienThue;
    public int traSach;
    public CallSlip(int maPH, String maTT, String maTV, String maSach, String ngay, int tienThue, int traSach)
    {
        this.maPH = maPH;
        this.maTT = maTT;
        this.maTV = maTV;
        this.maSach = maSach;
        this.ngay = ngay;
        this.tienThue = tienThue;
        this.traSach = traSach;
    }
    public CallSlip(){

    }
}
