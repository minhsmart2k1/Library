package com.example.library.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.dao.RevenueDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RevenueFragment extends Fragment {
    ImageView btnFromDate, btnToDate;
    Button btnRevenue;
    EditText edFromDate, edToDate;
    TextView tvRevenue;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    int mYear, mMonth, mDay;
    DatePickerDialog.OnDateSetListener mDateFromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            edFromDate.setText(sdf.format(c.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener mDateToDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
            edToDate.setText(sdf.format(c.getTime()));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doanh_thu, container, false);
        edFromDate = v.findViewById(R.id.edFromDate);
        edToDate = v.findViewById(R.id.edToDate);
        tvRevenue = v.findViewById(R.id.tvRevenue);
        btnFromDate = v.findViewById(R.id.btnFromDate);
        btnToDate = v.findViewById(R.id.btnToDate);
        btnRevenue = v.findViewById(R.id.btnRevenue);
        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(getActivity(), 0, mDateFromDate, mYear, mMonth, mDay);
                d.show();
            }
        });
        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(getActivity(), 0, mDateToDate, mYear, mMonth, mDay);
                d.show();
            }
        });
        btnRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tuNgay = edFromDate.getText().toString();
                String denNgay = edToDate.getText().toString();
                RevenueDAO revenueDAO = new RevenueDAO(getActivity());
                tvRevenue.setText("Revenue:  " + revenueDAO.getDoanhthu(tuNgay, denNgay) + " VND");
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

}
