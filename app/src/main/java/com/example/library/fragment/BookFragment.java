package com.example.library.fragment;

import static com.example.library.fragment.CallSlipFragment.getScreenWidth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.adapter.BookAdapter;
import com.example.library.adapter.TypeOfBookSpinerAdapter;
import com.example.library.dao.BookDAO;
import com.example.library.dao.TypeOfBookDAO;
import com.example.library.model.Book;
import com.example.library.model.Member;
import com.example.library.model.TypeOfBook;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {
    ListView lv;
    Dialog dialog;
    View view;
    Book book;
    String maloaiSach;
    SearchView sVB ;

    BookDAO bookDAO;
    ArrayList<Book> lists;
    BookAdapter adapter;

    EditText etNameB, etRentPrice, etDiscountB, etTacGia, etSoLuongCP, etViTri;
    FloatingActionButton fab;

    //spinner
    List<TypeOfBook> listTypeOfBook;
    TypeOfBookDAO typeOfBookDAO;
    TypeOfBookSpinerAdapter typeOfBookSpinerAdapter;


    int positionLS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sach, container, false);
        lv = view.findViewById(R.id.lvsach);
        fab = view.findViewById(R.id.fab);
        bookDAO = new BookDAO(getActivity());
        capNhatlv();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getActivity(), 0);
            }
        });
        lv.setOnItemLongClickListener((parent, view1, position, id) -> {
            book = lists.get(position);
            openDialog(getActivity(), 1);
            return false;
        });

        // Inflate the layout for this fragment
        sVB = view.findViewById(R.id.sVB);
        sVB.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return view;
    }


    protected void openDialog(final Context context, final int type) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.sach_dialog);
        TextView edMaSach = dialog.findViewById(R.id.edIdB);
        dialog.getWindow().setLayout((int) (getScreenWidth(getActivity()) * .9), ViewGroup.LayoutParams.WRAP_CONTENT);
        etNameB = dialog.findViewById(R.id.etNameB);
        etRentPrice = dialog.findViewById(R.id.etRentPrice);
        etDiscountB = dialog.findViewById(R.id.etDiscountB);
        etTacGia = dialog.findViewById(R.id.etTacGia);
        etSoLuongCP = dialog.findViewById(R.id.etSoLuongCP);
        etViTri = dialog.findViewById(R.id.etViTri);
        Button btnSavePM = dialog.findViewById(R.id.btnSaveB);
        Button btnCancelPM = dialog.findViewById(R.id.btnCancelB);

        //sp type of book
        typeOfBookDAO = new TypeOfBookDAO(context);
        listTypeOfBook=new ArrayList<TypeOfBook>();
        listTypeOfBook = (ArrayList<TypeOfBook>)typeOfBookDAO.getAll();
        Spinner spTypeTob = dialog.findViewById(R.id.spTypeTob);
        typeOfBookSpinerAdapter = new TypeOfBookSpinerAdapter(context, (ArrayList<TypeOfBook>) listTypeOfBook);
        spTypeTob.setAdapter(typeOfBookSpinerAdapter);
        spTypeTob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maloaiSach = listTypeOfBook.get(position).maLoai;
//                Toast.makeText(context, " Choose" + listTypeOfBook.get(position).tenLoai, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edMaSach.setEnabled(false);
        if (type != 0) {
            edMaSach.setText(String.valueOf(book.maSach));
            for (int i = 0; i < lists.size(); i++) {
                if (book.maLoai.equals(lists.get(i).maLoai)) {
                    positionLS = i;
                }

                Log.i("Demo", "posSach" + positionLS);
                spTypeTob.setSelection(positionLS);
            }

        }
        btnCancelPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSavePM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book = new Book();

                book.maLoai = maloaiSach;
                book.tenSach = etNameB.getText().toString();
                book.giamGia = Integer.parseInt(etDiscountB.getText().toString());
                book.tacGia = etTacGia.getText().toString();
                book.giaThue = Integer.parseInt(etRentPrice.getText().toString());
                book.SoluongCP = Integer.parseInt(etSoLuongCP.getText().toString());
                book.ViTri = etViTri.getText().toString();
                if (validate() > 0) {
                    if (type == 0) {
                        if (bookDAO.insert(book) > 0) {
                            Toast.makeText(context, "Added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        book.maSach = (edMaSach.getText().toString());
                        if (bookDAO.update(book) > 0) {
                            Toast.makeText(context, "Fixed", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    capNhatlv();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void xoaa(final String Id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookDAO.delete(Id);
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
        lists = (ArrayList<Book>) bookDAO.getAll();
        adapter = new BookAdapter(getActivity(), this, lists);
        lv.setAdapter(adapter);
    }

    public int validate() {
        int check = 1;
        if (etRentPrice.getText().length() == 0) {
            Toast.makeText(getContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
            check = -1;
        }
        else if(etNameB.getText().length() == 0) {
            Toast.makeText(getContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
            check = -1;
        }
        return check;
    }
}
