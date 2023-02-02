package com.example.library.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.adapter.BookSpinerAdapter;
import com.example.library.adapter.CallSlipAdapter;
import com.example.library.adapter.MemberSpinerAdapter;
import com.example.library.dao.BookDAO;
import com.example.library.dao.CallSlipDAO;
import com.example.library.dao.MemberDAO;
import com.example.library.model.Book;
import com.example.library.model.CallSlip;
import com.example.library.model.Member;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CallSlipFragment extends Fragment {
    ListView lv;
    Dialog dialog;
    View view;
    CallSlip callSlip;
    CallSlipDAO callSlipDAO;
    CallSlipAdapter adapter;
    ArrayList<CallSlip> list;

    FloatingActionButton fab;
    TextView etDate;
    TextView tvNgayTra;
    CheckBox chkReturned;
    Button btnSaveCs, btnCancelCs;
    String maThanhVien;
    String maSach;
    int positionMember, positionBook;
    //spinner
    BookSpinerAdapter bookSpinerAdapter;
    MemberSpinerAdapter memberSpinerAdapter;
    BookDAO bookDAO;
    MemberDAO memberDAO;
    ArrayList<Member> listMember;
    ArrayList<Book> listBooks;
    EditText etRentExpense;

    //btn ngay
    ImageView btnDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    int mYear, mMonth, mDay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phieu_muon, container, false);
        lv = v.findViewById(R.id.lvPhieuMuon);
        fab = v.findViewById(R.id.fab);
        callSlipDAO = new CallSlipDAO(getActivity());
        capNhatlv();
        fab.setOnClickListener(v1 -> openDialog(getActivity(), 0));
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            callSlip = list.get(position);
            openDialog(getActivity(), 1);
            return false;

        });
        return v;
    }
    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }
    protected void openDialog(Context context, final int type) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.phieu_muon_dialog);
        TextView edMaPM = dialog.findViewById(R.id.edIdCs);
        dialog.getWindow().setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);
        //ngay
        etDate = dialog.findViewById(R.id.etDate);
        tvNgayTra = dialog.findViewById(R.id.tvNgayTra);
        btnDate = dialog.findViewById(R.id.btnDate);
        chkReturned = dialog.findViewById(R.id.chkReturned);
        btnSaveCs = dialog.findViewById(R.id.btnSaveCs);
        btnCancelCs = dialog.findViewById(R.id.btnCancelCs);
        etRentExpense = dialog.findViewById(R.id.etRentExpense);

        DatePickerDialog.OnDateSetListener mDateNgay = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                GregorianCalendar c = new GregorianCalendar(mYear, mMonth, mDay);
                GregorianCalendar d = new GregorianCalendar(mYear, mMonth, mDay+7);
                etDate.setText(sdf.format(c.getTime()));
                tvNgayTra.setText(sdf.format(d.getTime()));
            }
        };
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(getActivity(), 0, mDateNgay, mYear, mMonth, mDay);
                d.show();
            }
        });
        //ngay

        Spinner spIdMem = dialog.findViewById(R.id.spIdMem);
        Spinner spNameB = dialog.findViewById(R.id.spNameB);
        memberDAO = new MemberDAO(context);
        listMember=new ArrayList<Member>();
        listMember= (ArrayList<Member>) memberDAO.getAll();
        memberSpinerAdapter = new MemberSpinerAdapter(context, (ArrayList<Member>) listMember);
        spIdMem.setAdapter(memberSpinerAdapter);
        spIdMem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maThanhVien = listMember.get(position).maTV;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bookDAO = new BookDAO(context);
        listBooks = (ArrayList<Book>) bookDAO.getAll();
        spNameB = dialog.findViewById(R.id.spNameB);
        bookSpinerAdapter = new BookSpinerAdapter(context, (ArrayList<Book>) listBooks);

        spNameB.setAdapter(bookSpinerAdapter);

        spNameB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maSach = listBooks.get(position).maSach;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edMaPM.setEnabled(false);
        if (type != 0) {
            edMaPM.setText(String.valueOf(callSlip.maPH));
            for (int i = 0; i < listMember.size(); i++)
                if (callSlip.maTV.equals((listMember.get(i).maTV))) {
                    positionMember = i;
                }
            spIdMem.setSelection(positionMember);


            for (int i = 0; i < listBooks.size(); i++)
                if (callSlip.maSach.equals((listBooks.get(i).maSach))) {
                    positionBook = i;
                }
            spNameB.setSelection(positionBook);
            etDate.setText("Rent Date:" + callSlip.ngay);
            if (callSlip.traSach == 1) {
                chkReturned.setChecked(true);
            } else {
                chkReturned.setChecked(false);
            }
        }
        btnCancelCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSaveCs.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                callSlip = new CallSlip();
                callSlip.maSach = maSach;
                callSlip.maTV = maThanhVien;
                callSlip.tienThue = Integer.parseInt(etRentExpense.getText().toString());
                //book.giaThue = Integer.parseInt(etRentPrice.getText().toString());
                callSlip.ngay = etDate.getText().toString();
                callSlip.ngayTra =tvNgayTra.getText().toString();

                if (chkReturned.isChecked()) {
                    callSlip.traSach = 1;
                } else {
                    callSlip.traSach = 0;
                }
                if (validate()>0){
                    if (type == 0) {
                        if (callSlipDAO.insert(callSlip) > 0) {
                            Toast.makeText(context, "Added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        callSlip.maPH = edMaPM.getText().toString();
                        if (callSlipDAO.update(callSlip) > 0) {
                            Toast.makeText(context, "Fixed", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                    capNhatlv();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void  xoa(final String Id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callSlipDAO.delete(Id);
                capNhatlv();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        builder.show();
    }

    void capNhatlv() {
        list = (ArrayList<CallSlip>) callSlipDAO.getAll();

        adapter = new CallSlipAdapter(getActivity(), this, list);
        lv.setAdapter(adapter);
    }

    public int validate() {
        int check = 1;
        if (etDate.getText().length() == 0) {
            Toast.makeText(getContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
            check = -1;
        }
        else if(etRentExpense.getText().length() == 0) {
            Toast.makeText(getContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
            check = -1;
        }
        return check;
    }


}
