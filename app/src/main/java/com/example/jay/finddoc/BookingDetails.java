package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingDetails extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse {

    TextView tvfname,tvlname,etbookdate,etbooktime,etplacedate,etplacetime,etpurpose;
    Button btnSave,btnModify,btncancel,btnDelete,btnuser;
    private static final int DSelect = 1, BSelect = 2, BDelete=3,BUpdate=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        //region Degine IDs
        setTitle("Booking Details");
        btnuser = (Button) findViewById(R.id.btnRowUser);
        btnSave = (Button) findViewById(R.id.btnRowSave);
        btnModify = (Button) findViewById(R.id.btnRowModify);
        btncancel = (Button) findViewById(R.id.btnRowCancel);
        btnDelete = (Button) findViewById(R.id.btnRowDel);

        tvfname = (TextView) findViewById(R.id.tvBDfname);
        tvlname = (TextView) findViewById(R.id.tvBDlname);
        etbookdate = (EditText) findViewById(R.id.tvBDbdate);
        etbooktime = (EditText) findViewById(R.id.tvBDbtime);
        etplacedate = (EditText) findViewById(R.id.tvBDpdate);
        etplacetime = (EditText) findViewById(R.id.tvBDptime);
        etpurpose = (EditText) findViewById(R.id.tvBDpurpose);

        filldoctor();
        fillbooking();
        fillFalse();
        //endregion
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SaveChanges
                Doctor doctor = (Doctor) getIntent().getSerializableExtra("Booking");
                String url = ServerCallAsyncTask.BASE_URL + "update_Booking";
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("pdate", doctor.getLastName());
                hashMap.put("ptime", doctor.getdEmail());
                hashMap.put("purpose", etpurpose.getText().toString());
                ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(BookingDetails.this, url, hashMap, BUpdate);
                asyncTask.execute();
                //endregion
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SetEnable True
                etbookdate.setEnabled(true);
                etbooktime.setEnabled(true);
                etplacedate.setEnabled(true);
                etplacetime.setEnabled(true);
                etpurpose.setEnabled(true);
                btnSave.setEnabled(true);
                btncancel.setEnabled(true);
                btnModify.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                btncancel.setVisibility(View.VISIBLE);
                //endregion
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillFalse();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region DeleteBooking
                Doctor doctor = (Doctor) getIntent().getSerializableExtra("Booking");
                String url = ServerCallAsyncTask.BASE_URL + "deletebook";
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("pdate", doctor.getLastName());
                hashMap.put("ptime", doctor.getdEmail());
                ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(BookingDetails.this, url, hashMap, BDelete);
                asyncTask.execute();
                //endregion
            }
        });

        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region BookingUser
                DoctorConstants.AdminBookUser="AdminBookUser";
                Doctor doctor1 = (Doctor) getIntent().getSerializableExtra("Booking");
                Intent intent=new Intent(BookingDetails.this,AdminUserDetails.class);
                intent.putExtra("email",doctor1.getdEmail());
                startActivity(intent);
                //endregion
            }
        });
    }

    private void fillFalse() {
        //region SetEnable False
        etbookdate.setEnabled(false);
        etbooktime.setEnabled(false);
        etplacedate.setEnabled(false);
        etplacetime.setEnabled(false);
        etpurpose.setEnabled(false);
        btnSave.setVisibility(View.INVISIBLE);
        btncancel.setVisibility(View.INVISIBLE);
        btnModify.setVisibility(View.VISIBLE);
        tvfname.setTextColor(Color.BLACK);
        tvlname.setTextColor(Color.BLACK);
        etbookdate.setTextColor(Color.BLACK);
        etbooktime.setTextColor(Color.BLACK);
        etplacetime.setTextColor(Color.BLACK);
        etplacedate.setTextColor(Color.BLACK);
        etpurpose.setTextColor(Color.BLACK);

        if(DoctorConstants.admin.equals("0"))
        {
            btnuser.setVisibility(View.INVISIBLE);
        }

        fillbooking();
        //endregion
    }

    private void fillbooking() {
        Doctor doctor = (Doctor) getIntent().getSerializableExtra("Booking");
        String url = ServerCallAsyncTask.BASE_URL + "find_single_bookdetail";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("pdate", doctor.getLastName());
        hashMap.put("ptime", doctor.gettime());
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(this, url, hashMap, BSelect);
        asyncTask.execute();
        String e=doctor.getFirstName();
    }

    private void filldoctor() {
        Doctor doctor = (Doctor) getIntent().getSerializableExtra("Booking");
        String url = ServerCallAsyncTask.BASE_URL + "finddocname";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("demail", doctor.getFirstName());
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(this, url, hashMap, DSelect);
        asyncTask.execute();
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

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if (flag == DSelect) {
            //region DoctorFill
            if (response.length() > 0) {
                SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PutDocDetail", response);
                editor.apply();

                String data = preferences.getString("PutDocDetail", "");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocDetail");

                    jsonObject = jsonArray.getJSONObject(0);
                    tvfname.setText(jsonObject.getString("fname"));
                    tvlname.setText(jsonObject.getString("lname"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid Doctor!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if(flag==BSelect) {
            //region BookingFill
            if (response.length() > 0) {
                SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PutBookDetail", response);
                editor.apply();

                String data = preferences.getString("PutBookDetail", "");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("BookRow");

                    jsonObject = jsonArray.getJSONObject(0);
                    etbookdate.setText(jsonObject.getString("appoint_date"));
                    etbooktime.setText(jsonObject.getString("appoint_time"));
                    etplacedate.setText(jsonObject.getString("place_date"));
                    etplacetime.setText(jsonObject.getString("place_time"));
                    etpurpose.setText(jsonObject.getString("purpose"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid Booking!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if (flag == BDelete) {
            //region Booking Delete
            if (response.trim().equals("1")) {
                Toast.makeText(this, "Your Booking Successfully Deleted.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyprofileActivity.class));
            } else {
                Toast.makeText(this, "Invalid Delete!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if (flag == BUpdate) {
            //region Booking Update
            if (response.trim().equals("1")) {
                Toast.makeText(this, "Your Booking Successfully Saved.", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),MyprofileActivity.class));
                fillFalse();
            } else {
                Toast.makeText(this, "Invalid Delete!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        }
    }
}
