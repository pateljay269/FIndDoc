package com.example.jay.finddoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jay on 21-03-2017.
 */

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private ArrayList<Doctor> doctorArrayList;
    private Context context;

    public DoctorAdapter(ArrayList<Doctor> doctorArrayList, Context context) {
        this.doctorArrayList=doctorArrayList;
        this.context=context;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_doctor,parent,false);
        DoctorViewHolder viewHolder=new DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DoctorViewHolder holder, int position) {
        final Doctor doctor=doctorArrayList.get(position);
        holder.tvFirstName.setText(doctor.getFirstName());
        holder.tvLastName.setText(doctor.getLastName());
        holder.dEmail=doctor.getdEmail();
        holder.tvDID.setText(doctor.getHead());
        if(DoctorConstants.AdminDoc.equals("AdminDoc"))
        {
            holder.btnRowBook.setText("Show");
        } else if(DoctorConstants.AdminDocBooking.equalsIgnoreCase("AdminDocBooking")) {
            holder.btnRowBook.setText("Cancel");
        }
        holder.btnRowBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DoctorConstants.AdminDoc.equals("AdminDoc")) {
                    Intent intent = new Intent(context, AdminDoctorDetails.class);
                    intent.putExtra("1DOCTOR", doctor);
                    context.startActivity(intent);
                } else if(DoctorConstants.AdminDocBooking.equalsIgnoreCase("AdminDocBooking")) {

                } else {
                    Intent intent = new Intent(context, BookAppointActivity.class);
                    intent.putExtra("DOCTOR", doctor);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorArrayList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder{
        TextView tvFirstName, tvLastName,tvDID;
        FButton btnRowBook;
        String dEmail;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            dEmail="";
            tvDID=(TextView)itemView.findViewById(R.id.tvDID);
            tvFirstName=(TextView)itemView.findViewById(R.id.tvFirstName);
            tvLastName=(TextView)itemView.findViewById(R.id.tvLastName);
            btnRowBook=(FButton)itemView.findViewById(R.id.btnRowBook);


        }
    }

}
