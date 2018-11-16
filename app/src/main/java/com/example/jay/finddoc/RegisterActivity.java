package com.example.jay.finddoc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.*;

import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse
{
    private static final int INSERT = 1;
    MaterialEditText etdob,etFname,etLname,etPass,etconfirmpass,etMobile,etEmail,etAddress,ettaluka,etdist,etpincode;
    Spinner blood_grp_spinner,gender_spinner,state_spinner;
    ImageButton btndob;
    Button btnRregister;
    String pass,conpass;
    int blood_pos,state_pos,gender_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //region Declare IDs
        setTitle("Patient Register");
        etconfirmpass= (MaterialEditText) findViewById(R.id.etConfirmpass);

        state_spinner= (Spinner) findViewById(R.id.spinner_Patstate);
        blood_grp_spinner= (Spinner) findViewById(R.id.blood_grp);
        gender_spinner= (Spinner) findViewById(R.id.gender);

        btnRregister=(Button) findViewById(R.id.btnRregister);
        btndob = (ImageButton) findViewById(R.id.btndob);

        etFname= (MaterialEditText) findViewById(R.id.etfname);
        etLname= (MaterialEditText) findViewById(R.id.etlname);
        etEmail= (MaterialEditText) findViewById(R.id.etemail);
        etPass= (MaterialEditText) findViewById(R.id.etpass);
        etMobile= (MaterialEditText) findViewById(R.id.etmob);
        etdob= (MaterialEditText) findViewById(R.id.etdob);
        etpincode = (MaterialEditText) findViewById(R.id.etPatpincode);
        etAddress= (MaterialEditText) findViewById(R.id.etaddress);
        ettaluka= (MaterialEditText) findViewById(R.id.etPatTaluka);
        etdist= (MaterialEditText) findViewById(R.id.etPatDistrict);
        //endregion

        btnRregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //region Register Click Event
                blood_pos =blood_grp_spinner.getSelectedItemPosition();
                state_pos =state_spinner.getSelectedItemPosition();
                gender_pos =gender_spinner.getSelectedItemPosition();

                pass=etPass.getText().toString();
                conpass=etconfirmpass.getText().toString();

                if(!etFname.getText().toString().equals("") && !etLname.getText().toString().equals("") &&
                        !etEmail.getText().toString().equals("") && !etPass.getText().toString().equals("") &&
                        !etMobile.getText().toString().equals("") && !etdob.getText().toString().equals("") &&
                        !etpincode.getText().toString().equals("") && !etAddress.getText().toString().equals("") &&
                        !ettaluka.getText().toString().equals("") && !etdist.getText().toString().equals("") &&
                        !(blood_pos ==0) && !(gender_pos ==0) && !(state_pos ==0))
                {

                    //region Check Password And Insert Data
                    if (pass.equals(conpass)) {
                    //region Insert Patient
                    String url = ServerCallAsyncTask.BASE_URL + "insert_patient";
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("fname", etFname.getText().toString());
                    hashMap.put("lname", etLname.getText().toString());
                    hashMap.put("email", etEmail.getText().toString());
                    hashMap.put("pass", etPass.getText().toString());
                    hashMap.put("mobile", etMobile.getText().toString());
                    hashMap.put("dob", etdob.getText().toString());
                    hashMap.put("blood_grp", blood_grp_spinner.getSelectedItem().toString());
                    hashMap.put("gender", gender_spinner.getSelectedItem().toString());
                    hashMap.put("pincode", etpincode.getText().toString());
                    hashMap.put("address", etAddress.getText().toString());
                    hashMap.put("taluka", ettaluka.getText().toString());
                    hashMap.put("district", etdist.getText().toString());
                    hashMap.put("stateofin", state_spinner.getSelectedItem().toString());
                    ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(RegisterActivity.this, url, hashMap, INSERT);
                    asyncTask.execute();

                    //endregion
                    } else {
                        Toast.makeText(RegisterActivity.this, "Please Enter Correct Confirm Password ", Toast.LENGTH_LONG).show();
                    }
                //endregion
                } else {
                    //region Show Error For EditText Empty
                    
                    if (blood_pos==0) {
                        ((TextView) blood_grp_spinner.getSelectedView()).setError("Field Required");
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

                    if(TextUtils.isEmpty(etPass.getText().toString())) {
                        etPass.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etMobile.getText().toString())) {
                        etMobile.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etdob.getText().toString())) {
                        etdob.setError("This Is Required");
                    }

                    if(TextUtils.isEmpty(etpincode.getText().toString())) {
                        etpincode.setError("This Is Required");
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

                    Toast.makeText(RegisterActivity.this, "Please Fill All Required Details", Toast.LENGTH_LONG).show();
                    //endregion
                }

                //endregion
            }
        });

        btndob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDiaxlog();
            }
        });
    }

    private void showDatePickerDiaxlog() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etdob.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },year, month, dayOfMonth).show();
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==INSERT)
        {
            if(response.trim().equals("1")) {
                startActivity(new Intent(getApplicationContext(), HomescreenActivity.class));
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
