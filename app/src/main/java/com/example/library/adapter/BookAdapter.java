package com.example.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.library.R;
import com.example.library.dao.TypeOfBookDAO;
import com.example.library.fragment.BookFragment;
import com.example.library.model.Book;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> implements Filterable {
    private Context context;
    ImageView btnDel;
    ArrayList<Book> lists;
    ArrayList<Book> listSe;
    BookFragment fragment;
    TextView tvIdB, tvIdTob, tvNameB, tvRentPrice, tvDiscountB, tvTacGia, tvSoLuong, tvViTri;
    TypeOfBookDAO typeOfBookDAO;

    public BookAdapter(Context context, BookFragment fragment, ArrayList<Book> lists) {
        super(context, 0, lists);
        this.context = context;
        this.lists = lists;
        this.listSe = lists;
        this.fragment = fragment;

    }



    @Override
    public int getCount() {
        if (lists == null) {
            return 0;
        } else {
            return lists.size();
        }
    }

    //filter search
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search  = constraint.toString();
                if (search.isEmpty()){
                    lists = listSe;
                }
                else{
                    ArrayList<Book> listTrungGian = new ArrayList<>();
                    for (Book Book11:listSe){
                        if (Book11.tacGia.toLowerCase().contains(search.toLowerCase())){
                            listTrungGian.add(Book11);
                        }
                    }
                    lists = (ArrayList<Book>) listTrungGian ;
                }
                FilterResults results = new FilterResults();
                results.values = lists ;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lists = (ArrayList<Book>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sach_item, null);
        }
        final Book book = lists.get(position);
        if (book != null) {
            //      TypeOfBook loaiSach=typeOfBookDAO.getID(String.valueOf(book.maLoai));
//            Book sach = lists.get(position);
            //tvIdB, tvIdTob, tvNameB, tvRentPrice
            tvIdB = view.findViewById(R.id.tvIdB);
            tvIdTob = view.findViewById(R.id.tvIdTob);
            tvNameB = view.findViewById(R.id.tvNameB);
            tvRentPrice = view.findViewById(R.id.tvRentPrice);
            tvDiscountB = view.findViewById(R.id.tvDiscountB);
            tvTacGia = view.findViewById(R.id.tvTacGia);
            tvSoLuong  = view.findViewById(R.id.tvSoLuong);
            tvViTri = view.findViewById(R.id.tvViTri);
            btnDel=view.findViewById(R.id.imgDeleteS);


            tvIdB.setText("Book ID: "+book.maSach );
            tvIdTob.setText(""+book.maLoai );
            tvNameB.setText(""+book.tenSach );
            tvRentPrice.setText(""+book.giaThue);
            tvDiscountB.setText(""+ book.giamGia);
            tvTacGia.setText(""+ book.tacGia);
            tvSoLuong.setText(""+ book.SoluongCP);
            tvViTri.setText(""+ book.ViTri);


        }
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.xoaa(String.valueOf(book.maSach));
//                fragment.xoa(String.valueOf(book.maSach));
            }
        });
        return view;
    }
}
