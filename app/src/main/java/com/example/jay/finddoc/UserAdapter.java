package com.example.jay.finddoc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

/**
 * Created by Jay on 27-03-2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>  {
    private ArrayList<Doctor> userArrayList;
    private Context context;

    public UserAdapter(ArrayList<Doctor> userArrayList, Context context) {
        this.userArrayList=userArrayList;
        this.context=context;
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        UserAdapter.UserViewHolder viewHolder=new UserAdapter.UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        final Doctor doctor=userArrayList.get(position);
        holder.tvFirstName.setText(doctor.getFirstName());
        holder.tvLastName.setText(doctor.getLastName());
        holder.dEmail=doctor.getdEmail();
        holder.dtime=doctor.gettime();
        holder.tvhead.setText(doctor.getHead());
        holder.btnRowshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DoctorConstants.AdminUser.equalsIgnoreCase("AdminUser")) {
                    Intent intent=new Intent(context,AdminUserDetails.class);
                    intent.putExtra("UserData",doctor);
                    context.startActivity(intent);
                } else if(DoctorConstants.AdminBook.equalsIgnoreCase("AdminBook")) {
                    Intent intent=new Intent(context,BookingDetails.class);
                    intent.putExtra("Booking",doctor);
                    context.startActivity(intent);
                } else if(DoctorConstants.AdminLogSelect.equalsIgnoreCase("AdminLogSelect")) {
                    Intent intent=new Intent(context,AdminUserDetails.class);
                    intent.putExtra("UserData",doctor);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView tvhead,tvFirstName, tvLastName;
        FButton btnRowshow;
        String dEmail,dtime;

        public UserViewHolder(View itemView) {
            super(itemView);
            dEmail="";
            dtime="";
            tvhead=(TextView) itemView.findViewById(R.id.tvhead);
            tvFirstName=(TextView)itemView.findViewById(R.id.tvbUfname);
            tvLastName=(TextView)itemView.findViewById(R.id.tvbUlname);
            btnRowshow=(FButton)itemView.findViewById(R.id.btnRowUShow);
        }
    }
}
