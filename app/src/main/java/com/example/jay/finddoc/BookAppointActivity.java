package com.example.jay.finddoc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class BookAppointActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse
{

    private static final int bINSERT = 1;
    MaterialEditText etbookemail,etbookmobile,etbookdname,etbookdate,etbooktime;
    MultiAutoCompleteTextView etbookpurpose;
    ImageButton btnappointdate,btnappointtime;
    Button btnbook;
    Calendar c;
    SimpleDateFormat df;
    String now_date,now_time;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint);

        //region Declare IDs
        setTitle("Booking Appointment");
        doctor=(Doctor)getIntent().getSerializableExtra("DOCTOR");
        btnbook=(Button) findViewById(R.id.btnbook);
        btnappointdate = (ImageButton) findViewById(R.id.btnappointdate);
        btnappointtime = (ImageButton) findViewById(R.id.btnappointtime);

        etbookemail=(MaterialEditText) findViewById(R.id.etbookemail);
        etbookmobile=(MaterialEditText) findViewById(R.id.etbookmob);
        etbookdname=(MaterialEditText) findViewById(R.id.etbookdname);
        etbookdate = (MaterialEditText) findViewById(R.id.etbookdate);
        etbooktime=(MaterialEditText) findViewById(R.id.etbooktime);
        etbookpurpose=(MultiAutoCompleteTextView) findViewById(R.id.etbookpurpose);
        etbookdname.setText(doctor.getFirstName()+" "+doctor.getLastName());
        //endregion

        //region Current Date/Time
        c = Calendar.getInstance();

        df = new SimpleDateFormat("dd/MM/yyyy");
        now_date = df.format(c.getTime());

        df = new SimpleDateFormat("HH:mm:ss");
        now_time = df.format(c.getTime());
        //endregion

        fillDetail();

        btnappointdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDiaxlog();
            }
        });

        btnappointtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDiaxlog();
            }
        });

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!etbookemail.getText().toString().equals("") && !etbookmobile.getText().toString().equals("") &&
                        !etbookdname.getText().toString().equals("") && !etbookdate.getText().toString().equals("") &&
                        !etbooktime.getText().toString().equals("") && !etbookpurpose.getText().toString().equals("") ) {
                    //region AppointmentBooking
                    String url = ServerCallAsyncTask.BASE_URL + "book_appoint";
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("pemail", etbookemail.getText().toString());
                    hashMap.put("pmob", etbookmobile.getText().toString());
                    hashMap.put("dname", doctor.getdEmail());
                    hashMap.put("dfname", doctor.getFirstName());
                    hashMap.put("dlname", doctor.getLastName());
                    hashMap.put("bookdate", etbookdate.getText().toString());
                    hashMap.put("booktime", etbooktime.getText().toString());
                    hashMap.put("purpose", etbookpurpose.getText().toString());
                    hashMap.put("currdate", now_date);
                    hashMap.put("currtime", now_time);
                    ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(BookAppointActivity.this, url, hashMap, bINSERT);
                    asyncTask.execute();
                    //endregion
                } else {
                    //region Show Error For EditText Empty

                    if(TextUtils.isEmpty(etbookemail.getText().toString())) {
                        etbookemail.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etbookmobile.getText().toString())) {
                        etbookmobile.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etbookdname.getText().toString())) {
                        etbookdname.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etbookdate.getText().toString())) {
                        etbookdate.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etbooktime.getText().toString())) {
                        etbooktime.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etbookpurpose.getText().toString())) {
                        etbookpurpose.setError("This Is Required");
                    }

                    Toast.makeText(BookAppointActivity.this, "Please Fill All Required Details", Toast.LENGTH_LONG).show();
                    //endregion
                }
            }
        });
    }

    private void fillDetail() {
        SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
        String data=preferences.getString("USER","");
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            jsonObject=jsonArray.getJSONObject(0);
            etbookemail.setText(jsonObject.getString("email"));
            etbookmobile.setText(jsonObject.getString("mobileno"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTimePickerDiaxlog() {
        c = Calendar.getInstance();
        int mHour,mMinute;
        
        mHour = c.get(Calendar.HOUR);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog=new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                etbooktime.setText(hourOfDay+":"+minute);
            }
        }, mHour,mMinute,true);
        timePickerDialog.show();
    }

    private void showDatePickerDiaxlog() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(BookAppointActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etbookdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year, month, dayOfMonth).show();
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
        if(flag==bINSERT)
        {
            if(response.trim().equals("1")) {
                Toast.makeText(BookAppointActivity.this, "Your Request Is Place Sucessfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), PatientDashActivity.class));
            }else
            {
                Toast.makeText(this, "Booking Failed : "+response, Toast.LENGTH_LONG).show();
            }
        }
    }

}
