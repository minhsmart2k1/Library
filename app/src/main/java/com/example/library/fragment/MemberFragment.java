package com.example.library.fragment;

import static com.example.library.fragment.CallSlipFragment.getScreenWidth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.adapter.MemberAdapter;
import com.example.library.dao.MemberDAO;
import com.example.library.model.Member;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MemberFragment extends Fragment {
    public static MemberDAO dao;
    ListView lv;
    ArrayList<Member> list;
    FloatingActionButton fab;
    Dialog dialog;
    EditText etNameM, etBirthM;
    TextView tvIdM;
    //search view
    SearchView searchView ;
    Button btnSaveM, btnCacelM;
    MemberAdapter adapter;
    Member member;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_thanh_vien, container, false);
        lv = v.findViewById(R.id.lvThanhVien);
        fab = v.findViewById(R.id.fab);
        dao = new MemberDAO(getActivity());
        capNhatlv();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getActivity(), 0);
            }
        });
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            member = list.get(position);
            openDialog(getActivity(), 1);
            return false;
        });
        // Inflate the layout for this fragment
        //search
        searchView = v.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return v;
    }

    protected void openDialog(final Context context, final int type) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.thanh_vien_dialog);
        dialog.getWindow().setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);
        tvIdM = dialog.findViewById(R.id.tvIdM);
        etNameM = dialog.findViewById(R.id.etNameM);
        etBirthM = dialog.findViewById(R.id.etBirthM);
        btnCacelM = dialog.findViewById(R.id.btnCacelM);
        btnSaveM = dialog.findViewById(R.id.btnSaveM);
        tvIdM.setEnabled(false);
        if (type != 0) {
            tvIdM.setText(String.valueOf(member.maTV));
            etNameM.setText(member.hoTen);
            etBirthM.setText(member.namSinh);
        }
        btnCacelM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSaveM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member = new Member();
                member.hoTen = etNameM.getText().toString();
                member.namSinh = etBirthM.getText().toString();
                member.maTK = "minhsmart";
                member.MSSV = 19200377;
                if (validate() > 0) {
                    if (type == 0) {
                        // insert
                        if (dao.insert(member) > 0) {
                            Toast.makeText(context, "Add Sucessfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Add failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // update
                        member.maTV = (tvIdM.getText().toString());
                        if (dao.update(member) > 0) {
                            Toast.makeText(context, "Edit Sucessfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Edit Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                    capNhatlv();
                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }

    public void xoa(final String Id) {
        // sd alert
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.delete(Id);
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
        list = (ArrayList<Member>) dao.getAll();
        adapter = new MemberAdapter(getActivity(), this, list);
        lv.setAdapter(adapter);
    }

    public int validate() {
        int check = 1;
        int currentTime = Calendar.getInstance().get(Calendar.YEAR);
        int  myNum = Integer.parseInt(etBirthM.getText().toString());
        if (etNameM.getText().length() == 0 || etBirthM.getText().length() == 0) {
            Toast.makeText(getContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
            check = -1;
        } else
        if( etNameM.getText().length() < 5 || etNameM.getText().length() > 15){
            Toast.makeText(getContext(), "Name's length must be between 5 and 15 letters", Toast.LENGTH_LONG).show();
            check = -1;
        }
        else
        if(!etNameM.getText().toString().substring(0,1).toUpperCase().equals(etNameM.getText().toString().substring(0,1))){
            Toast.makeText(getContext(), "Please uppercase first letter of Name", Toast.LENGTH_LONG).show();
            check = -1;
        }
        else
        if (1900 > myNum || myNum > currentTime){
            Toast.makeText(getContext(), "Invalid year", Toast.LENGTH_LONG).show();
            check = -1;
        }

        return check;
    }
}
