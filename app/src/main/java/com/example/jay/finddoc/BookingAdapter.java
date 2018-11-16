package com.example.jay.finddoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

/**
 * Created by Jay on 24-03-2017.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private static final int Delete = 1;
    private ArrayList<Doctor> bookingArrayList;
    private Context context;

    public BookingAdapter(ArrayList<Doctor> bookingArrayList, Context context) {
        this.bookingArrayList=bookingArrayList;
        this.context=context;
    }

    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book,parent,false);
        BookingAdapter.BookingViewHolder viewHolder=new BookingAdapter.BookingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BookingAdapter.BookingViewHolder holder, int position) {

        final Doctor doctor=bookingArrayList.get(position);
        holder.tvDID.setText(doctor.getHead());
        holder.tvFirstName.setText(doctor.getFirstName());
        holder.tvLastName.setText(doctor.getLastName());
        holder.dEmail=doctor.getdEmail();
        holder.dTime=doctor.gettime();
        holder.btnRowShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BookingDetails.class);
                intent.putExtra("Booking",doctor);
                context.startActivity(intent);

            }
        });
    }

    public int getItemCount() {
        return bookingArrayList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder{
        TextView tvFirstName, tvLastName,tvDID;
        FButton btnRowShow;
        String dEmail,dTime;

        public BookingViewHolder(View itemView) {
            super(itemView);
            dEmail="";
            dTime="";
            tvDID=(TextView)itemView.findViewById(R.id.tvBid);
            tvFirstName=(TextView)itemView.findViewById(R.id.tvbBfname);
            tvLastName=(TextView)itemView.findViewById(R.id.tvbBlname);
            btnRowShow=(FButton)itemView.findViewById(R.id.btnRowShow);
        }
    }
}
