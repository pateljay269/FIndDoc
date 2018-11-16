package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCalender extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etfind;
    private ArrayList<Doctor> doctorArrayList, tempArrayList;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calender);

        etfind=(EditText) findViewById(R.id.etmycalfind);
        recyclerView=(RecyclerView)findViewById(R.id.rvmycal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        if(DoctorConstants.admin.equals("admin")){
            //region Admin Operation
            SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
            if(DoctorConstants.AdminUserBook.equalsIgnoreCase("AdminUserBook")) {
                //region AdminUser Bookings
                String data = preferences.getString("PutUserBook", "");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocBook");
                    doctorArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setHead("Booking No: "+String.valueOf(i+1));
                        doctor.setFirstName(jsonObject.getString("d_email"));
                        doctor.setLastName(jsonObject.getString("place_date"));
                        doctor.setdEmail(jsonObject.getString("p_email"));
                        doctor.settime(jsonObject.getString("place_time"));
                        doctorArrayList.add(doctor);
                    }

                    tempArrayList=doctorArrayList;
                    BookingAdapter adapter = new BookingAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
            } else if(DoctorConstants.AdminBook.equalsIgnoreCase("AdminBook")) {
                //region AdminBook
                String data2 = preferences.getString("PutAdminBook", "");
                try {
                    JSONObject jsonObject = new JSONObject(data2);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocBook");
                    doctorArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setFirstName(jsonObject.getString("d_email"));
                        doctor.setLastName(jsonObject.getString("place_date"));
                        doctor.settime(jsonObject.getString("place_time"));
                        doctor.setdEmail(jsonObject.getString("p_email"));
                        doctor.setHead(jsonObject.getString("p_email"));
                        doctorArrayList.add(doctor);
                    }
                    tempArrayList=doctorArrayList;
                    UserAdapter adapter = new UserAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
            } else if(DoctorConstants.AdminUser.equalsIgnoreCase("AdminUser")) {
                //region AdminUsers
                String data1 = preferences.getString("PutAdminUser", "");
                try {
                    JSONObject jsonObject = new JSONObject(data1);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocUser");
                    doctorArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setFirstName(jsonObject.getString("email"));
                        doctor.setLastName(jsonObject.getString("mobileno"));
                        doctor.setdEmail(jsonObject.getString("email"));
                        doctor.setHead("User: "+String.valueOf(i + 1));
                        doctorArrayList.add(doctor);
                    }
                    tempArrayList=doctorArrayList;
                    UserAdapter adapter = new UserAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //endregion
            } else if(DoctorConstants.AdminDoc.equalsIgnoreCase("AdminDoc")) {
                //region AdminDoctor
                String data1 = preferences.getString("PutAdminDoc", "");
                try {
                    JSONObject jsonObject = new JSONObject(data1);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocAll");
                    doctorArrayList= new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setFirstName(jsonObject.getString("fname"));
                        doctor.setLastName(jsonObject.getString("lname"));
                        doctor.setdEmail(jsonObject.getString("d_email"));
                        doctor.setHead("Doctor: "+String.valueOf(i + 1));
                        doctorArrayList.add(doctor);
                    }
                    tempArrayList=doctorArrayList;
                    adapter = new DoctorAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
            }  else if(DoctorConstants.AdminDocBooking.equalsIgnoreCase("AdminDocBooking")) {
                //region AdminDoctorBookings
                String data2 = preferences.getString("PutAdminDocBook", "");
                try {
                    JSONObject jsonObject = new JSONObject(data2);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocAll");
                    doctorArrayList= new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setFirstName(jsonObject.getString("p_email"));
                        doctor.setLastName(jsonObject.getString("status"));
                        doctor.setdEmail(jsonObject.getString("p_email"));
                        doctor.setHead(jsonObject.getString("appoint_date")+" "+jsonObject.getString("appoint_time"));
                        doctorArrayList.add(doctor);
                    }
                    tempArrayList=doctorArrayList;
                    adapter = new DoctorAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //endregion
            } else if(DoctorConstants.AdminLogSelect.equalsIgnoreCase("AdminLogSelect")) {
                //region AdminUserLog
                String data1 = preferences.getString("PutUserLog", "");
                try {
                    JSONObject jsonObject = new JSONObject(data1);
                    JSONArray jsonArray = jsonObject.getJSONArray("UserLog");
                    doctorArrayList= new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setFirstName(jsonObject.getString("p_email"));
                        doctor.setLastName(jsonObject.getString("state"));
                        doctor.setdEmail(jsonObject.getString("p_email"));
                        doctor.setHead(jsonObject.getString("login_date")+" "+jsonObject.getString("login_time"));
                        doctorArrayList.add(doctor);
                    }
                    tempArrayList=doctorArrayList;
                    UserAdapter adapter = new UserAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //endregion
            }
            //endregion
        } else if() {
            //region User Bookings
            SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
            String data = preferences.getString("PutBook", "");
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("DocBook");
                ArrayList<Doctor> bookingArrayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Doctor doctor = new Doctor();
                    doctor.setHead("Booking No: "+String.valueOf(i+1));
                    doctor.setFirstName(jsonObject.getString("d_email"));
                    doctor.setLastName(jsonObject.getString("place_date"));
                    doctor.settime(jsonObject.getString("place_time"));
                    bookingArrayList.add(doctor);
                }

                BookingAdapter adapter = new BookingAdapter(bookingArrayList, MyCalender.this);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //endregion
        } else if() {
            //region AdminDoctor
            String data1 = preferences.getString("PutAdminDoc", "");
            try {
                JSONObject jsonObject = new JSONObject(data1);
                JSONArray jsonArray = jsonObject.getJSONArray("DocAll");
                doctorArrayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Doctor doctor = new Doctor();
                    doctor.setFirstName(jsonObject.getString("fname"));
                    doctor.setLastName(jsonObject.getString("lname"));
                    doctor.setdEmail(jsonObject.getString("d_email"));
                    doctor.setHead("Doctor: " + String.valueOf(i + 1));
                    doctorArrayList.add(doctor);
                }
                tempArrayList = doctorArrayList;
                adapter = new DoctorAdapter(doctorArrayList, MyCalender.this);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //endregion
        }
        etfind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //region TextChangedArrayList
                String dName=etfind.getText().toString();
                if(dName.isEmpty()) {
                    doctorArrayList=tempArrayList;
                } else {
                    doctorArrayList = new ArrayList<Doctor>();
                    for (Doctor doctor :
                            tempArrayList) {
                        if(doctor.getFirstName().toLowerCase().contains(dName)
                                || doctor.getLastName().toLowerCase().contains(dName)){
                            doctorArrayList.add(doctor);
                        }
                    }
                }
                //endregion

                //region Add Adapter On RecyclerView
                if(DoctorConstants.AdminUserBook.equalsIgnoreCase("AdminUserBook")) {

                    BookingAdapter adapter = new BookingAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } else if(DoctorConstants.AdminBook.equalsIgnoreCase("AdminBook")) {

                    UserAdapter adapter = new UserAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } else if(DoctorConstants.AdminUser.equalsIgnoreCase("AdminUser")) {

                    UserAdapter adapter = new UserAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } else if(DoctorConstants.AdminDoc.equalsIgnoreCase("AdminDoc")) {

                    adapter = new DoctorAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                } else if(DoctorConstants.AdminLogSelect.equalsIgnoreCase("AdminLogSelect")) {

                    UserAdapter adapter = new UserAdapter(doctorArrayList, MyCalender.this);
                    recyclerView.setAdapter(adapter);
                }  else if(DoctorConstants.AdminDocBooking.equalsIgnoreCase("AdminDocBooking")) {

                        adapter = new DoctorAdapter(doctorArrayList, MyCalender.this);
                        recyclerView.setAdapter(adapter);
                }
                //endregion
            }
        });
    }

    //region Home_Action_Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeonly, menu);
        DoctorConstants.mainMenu=menu;
        return true; }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            if(DoctorConstants.admin.equals("admin")) {
                startActivity(new Intent(getApplicationContext(), AdminDash.class));
            } else {
                startActivity(new Intent(getApplicationContext(), PatientDashActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

}
