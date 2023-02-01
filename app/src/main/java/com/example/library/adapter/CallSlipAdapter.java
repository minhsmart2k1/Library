package com.example.library.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.ConnectionHelper;
import com.example.library.R;
import com.example.library.dao.BookDAO;
import com.example.library.dao.MemberDAO;
import com.example.library.fragment.CallSlipFragment;
import com.example.library.model.Book;
import com.example.library.model.CallSlip;
import com.example.library.model.Member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CallSlipAdapter extends ArrayAdapter<CallSlip> {
    private Context context;
    CallSlipFragment fragment;
    private ArrayList<CallSlip>lists;
    TextView tvMaPM, tvTenTv, tvTenSach, tvTienThue, tvNgay, tvNgayTra, tvTraSach;
    ImageView imgdel;
    BookDAO bookDAO;
    MemberDAO memberDAO;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public CallSlipAdapter(@NonNull Context context, CallSlipFragment fragment, ArrayList<CallSlip> lists) {
        super(context, 0,lists);
        this.context = context;
        this.fragment = fragment;
        this.lists = lists;
    }
    @Override
    public int getCount() {
        if (lists==null)
        {return 0;}
        else {
            return lists.size();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if (v==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.phieu_muon_item,null);

        }
        final CallSlip item=lists.get(position);
        if (item != null) {
            String tenSach = null, hoTen = null;
            Connection connect;
            String ConnectionResult = "";

            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                if(connect != null){
                    String query = "SELECT BanSaoSach.MaSachCP, BanSaoSach.MaSach, Sach.TenSach, NguoiMuon.MaNguoiMuon, TaiKhoan.HoTen\n" +
                            "FROM BanSaoSach, Sach, NguoiMuon, TaiKhoan, ThongTinMuonTraSach\n" +
                            "WHERE BanSaoSach.MaSach = Sach.MaSach\n" +
                            "\tand BanSaoSach.MaSachCP = ThongTinMuonTraSach.MaSachCP\n" +
                            "\tand ThongTinMuonTraSach.MaNguoiMuon = NguoiMuon.MaNguoiMuon\n" +
                            "\tand NguoiMuon.MaTK = TaiKhoan.MaTK\n" +
                            "\tand BanSaoSach.MaSachCP = '" + item.maSach + "'\n" +
                            "\tand ThongTinMuonTraSach.MaTTMuonTra = '" + item.maPH + "'";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    while (rs.next())
                    {
                        tenSach = rs.getString(3);
                        hoTen = rs.getString(5);
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

            CallSlip callSlip =lists.get(position);
            tvMaPM=v.findViewById(R.id.tvMaPM);
            bookDAO =new BookDAO(context);
            Book book = bookDAO.getID(String.valueOf(item.maSach));
            tvTenSach=v.findViewById(R.id.tvTenSach);
            memberDAO =new MemberDAO(context);
            Member member = memberDAO.getID(String.valueOf(item.maTV));
            tvTenTv=v.findViewById(R.id.tvTenTV1);
            tvTienThue=v.findViewById(R.id.tvTienThue);
            tvNgay=v.findViewById(R.id.tvNgayPM);
            tvNgayTra = v.findViewById(R.id.tvNgayTra);

            tvMaPM.setText(""+ callSlip.maPH);
            tvTenSach.setText(""+ tenSach);
            tvTenTv.setText(""+ hoTen);
            tvTienThue.setText(""+ callSlip.tienThue);
            tvNgay.setText(""+ callSlip.ngay);
            tvNgayTra.setText(""+ callSlip.ngayTra);
            tvTraSach=v.findViewById(R.id.tvTraSach);
            if (item.traSach==1){
                tvTraSach.setTextColor(Color.WHITE);
                tvTraSach.setText("Da tra book");
            }
            else {
                tvTraSach.setTextColor(Color.rgb(172, 223, 135));
                tvTraSach.setText("Chua tra book");
            }

        }
        imgdel=v.findViewById(R.id.imgDeletePM);
        imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.xoa(String.valueOf(item.maPH));
            }
        });
        return v;
    }

}

