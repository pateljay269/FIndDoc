package com.example.jay.finddoc;

import android.app.*;
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

import java.util.*;


public class RegisterDocActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    private static final int dINSERT = 1;
    MaterialEditText etdob,etFname,etLname,etMobile,etEmail,etAddress,ettaluka,etdist;
    Spinner special_spinner,gender_spinner,state_spinner;
    ImageButton btndob;
    Button btnRregister;
    int special_pos,state_pos,gender_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doc);

        //region Declare IDs
        setTitle("Doctor Register");
        etFname= (MaterialEditText) findViewById(R.id.etDocfname);
        etLname= (MaterialEditText) findViewById(R.id.etDoclname);
        etEmail= (MaterialEditText) findViewById(R.id.etDocemail);
        etMobile= (MaterialEditText) findViewById(R.id.etDocmob);
        etdob= (MaterialEditText) findViewById(R.id.etDocdob);
        etAddress= (MaterialEditText) findViewById(R.id.etDocaddress);
        ettaluka= (MaterialEditText) findViewById(R.id.etDocTaluka);
        etdist= (MaterialEditText) findViewById(R.id.etDocDistrict);


        state_spinner= (Spinner) findViewById(R.id.Docstate);
        special_spinner= (Spinner) findViewById(R.id.Docspecialist);
        gender_spinner= (Spinner) findViewById(R.id.Docgender);


        btndob = (ImageButton) findViewById(R.id.btnDocdob);
        btnRregister=(Button) findViewById(R.id.btnDocRregister);
        //endregion

        btndob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDiaxlog();
            }
        });

        btnRregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //region Register Click Event

                special_pos =special_spinner.getSelectedItemPosition();
                state_pos =state_spinner.getSelectedItemPosition();
                gender_pos =gender_spinner.getSelectedItemPosition();

                if (!etFname.getText().toString().equals("") && !etLname.getText().toString().equals("") &&
                        !etEmail.getText().toString().equals("") && !etMobile.getText().toString().equals("")
                        && !etdob.getText().toString().equals("") && !etAddress.getText().toString().equals("")
                        && !ettaluka.getText().toString().equals("") && !etdist.getText().toString().equals("")
                        && !(special_pos == 0) && !(gender_pos == 0) && !(state_pos == 0)) {

                        //region Insert Doctor
                        String url = ServerCallAsyncTask.BASE_URL + "doctor_reg";
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("fname", etFname.getText().toString());
                        hashMap.put("lname", etLname.getText().toString());
                        hashMap.put("email", etEmail.getText().toString());
                        hashMap.put("dob", etdob.getText().toString());
                        hashMap.put("mno", etMobile.getText().toString());
                        hashMap.put("gender", gender_spinner.getSelectedItem().toString());
                        hashMap.put("address", etAddress.getText().toString());
                        hashMap.put("taluka", ettaluka.getText().toString());
                        hashMap.put("district", etdist.getText().toString());
                        hashMap.put("stateofin", state_spinner.getSelectedItem().toString());
                        hashMap.put("special", special_spinner.getSelectedItem().toString());
                        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(RegisterDocActivity.this, url, hashMap, dINSERT);
                        asyncTask.execute();
                        //endregion

                    Toast.makeText(RegisterDocActivity.this, "Your Data Is Submitted", Toast.LENGTH_LONG).show();
                } else {
                    //region Show Error For EditText Empty

                    if (special_pos==0) {
                        ((TextView) special_spinner.getSelectedView()).setError("Field Required");
                    }

                    if (gender_pos==0) {
                        ((TextView) gender_spinner.getSelectedView()).setError("Field Required");
                    }

                    if (state_pos==0) {
                        ((TextView) state_spinner.getSelectedView()).setError("Field Required");
                    }

                    if(TextUtils.isEmpty(etFname.getText().toString())) {
                        etFname.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etLname.getText().toString())) {
                        etLname.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etEmail.getText().toString())) {
                        etEmail.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etMobile.getText().toString())) {
                        etMobile.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etdob.getText().toString())) {
                        etdob.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etAddress.getText().toString())) {
                        etAddress.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(ettaluka.getText().toString())) {
                        ettaluka.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etdist.getText().toString())) {
                        etdist.setError("This Is Required");
                    }

                    Toast.makeText(RegisterDocActivity.this, "Please Fill All Required Details", Toast.LENGTH_LONG).show();
                    //endregion
                }

                //endregion
            }
        });
    }

    private void showDatePickerDiaxlog() {

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(RegisterDocActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etdob.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year, month, dayOfMonth).show();
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==dINSERT)
        {
            if(response.trim().equals("1")) {
                startActivity(new Intent(getApplicationContext(), AdminDash.class));
            }else
            {
                Toast.makeText(this, "Registration Failed : "+response, Toast.LENGTH_LONG).show();
            }
        }
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
