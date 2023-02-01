package com.example.library.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.library.ConnectionHelper;
import com.example.library.R;
import com.example.library.model.Top;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TopAdapter extends ArrayAdapter<Top> {
    private Context context;

    private ArrayList<Top> lists;
    TextView tvSach,tvSL;

    public TopAdapter(@NonNull Context context, ArrayList<Top> lists) {
        super(context, 0,lists);
        this.context=context;
        this.lists=lists;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if (v==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.top_item,null);
        }
        final Top item=lists.get(position);
        if (item!=null){
            Connection connect;
            String ConnectionResult = "";
            String tenSach = null, maSach = null;
            int sl = 0;
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                if(connect != null) {
                    String sqlCheck = "SELECT * FROM Sach";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(sqlCheck);
                    while(rs.next()){
                        tenSach = rs.getString(2);
                        sl = rs.getInt(7);
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
            tvSach=v.findViewById(R.id.tvSach);
            tvSach.setText("Book name: "+tenSach);
            tvSL=v.findViewById(R.id.tvSoLuong);
            tvSL.setText("Quantity: "+sl);
        }
        return v;
    }
}
