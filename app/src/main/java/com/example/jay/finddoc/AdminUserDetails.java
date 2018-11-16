package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class AdminUserDetails extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    private static final int UPDATE = 1,BSelect=2,UDelete=3,USave=4,Ulog=5;
    Button btnSave, btnModify, btndelete, btncancel,btnuserbook,btnuserlog;
    MaterialEditText etpass, etFname, etLname, etMobile, etAddress, etpin, ettaluka, etdistrict, etemail, etdob;
    Spinner spinner_newState, spinnerblood, spinnergender;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);

        //region Declare IDs
        setTitle("User Details");
        btnuserbook = (Button) findViewById(R.id.btnadminRowbook);
        btnuserlog = (Button) findViewById(R.id.btnadminRowlog);
        btnSave = (Button) findViewById(R.id.btnadminRowSave);
        btnModify = (Button) findViewById(R.id.btnadminRowModify);
        btndelete = (Button) findViewById(R.id.btnadminRowDel);
        btncancel = (Button) findViewById(R.id.btnadminRowCancel);

        etFname = (MaterialEditText) findViewById(R.id.etadminfname);
        etLname = (MaterialEditText) findViewById(R.id.etadminlname);
        etemail = (MaterialEditText) findViewById(R.id.etadminemail);
        etpass = (MaterialEditText) findViewById(R.id.etadminpass);
        etdob = (MaterialEditText) findViewById(R.id.etadmindob);
        etMobile = (MaterialEditText) findViewById(R.id.etadminmob);
        etAddress = (MaterialEditText) findViewById(R.id.etadminaddress);
        etpin = (MaterialEditText) findViewById(R.id.etadminpincode);
        ettaluka = (MaterialEditText) findViewById(R.id.etadminTaluka);
        etdistrict = (MaterialEditText) findViewById(R.id.etadminDistrict);

        spinner_newState = (Spinner) findViewById(R.id.spinner_adminstate);
        spinnerblood = (Spinner) findViewById(R.id.spinner_adminblood);
        spinnergender = (Spinner) findViewById(R.id.spinner_admingender);

        fillFalse();
        filluser();
        //endregion

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SetEnable True
                etFname.setEnabled(true);
                etLname.setEnabled(true);
                etemail.setEnabled(true);
                etpass.setEnabled(true);
                etdob.setEnabled(true);
                etMobile.setEnabled(true);
                etAddress.setEnabled(true);
                etpin.setEnabled(true);
                ettaluka.setEnabled(true);
                etdistrict.setEnabled(true);

                spinner_newState.setEnabled(true);
                spinnerblood.setEnabled(true);
                spinnergender.setEnabled(true);

                btnSave.setVisibility(View.VISIBLE);
                btncancel.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.INVISIBLE);
                //endregion
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillFalse();
                filluser();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region DeleteUser
                fillFalse();
                String url = ServerCallAsyncTask.BASE_URL + "deleteuser";
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email", etemail.getText().toString());
                ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(AdminUserDetails.this, url, hashMap, UDelete);
                asyncTask.execute();
                //endregion
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SaveChanges
                String url = ServerCallAsyncTask.BASE_URL + "update_Admin_patient";
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", id);
                hashMap.put("fname", etFname.getText().toString());
                hashMap.put("lname", etLname.getText().toString());
                hashMap.put("email", etemail.getText().toString());
                hashMap.put("pass", etpass.getText().toString());
                hashMap.put("mob", etMobile.getText().toString());
                hashMap.put("pincode", etpin.getText().toString());
                hashMap.put("dob", etdob.getText().toString());
                hashMap.put("gender", spinnergender.getSelectedItem().toString());
                hashMap.put("address", etAddress.getText().toString());
                hashMap.put("taluka", ettaluka.getText().toString());
                hashMap.put("district", etdistrict.getText().toString());
                hashMap.put("stateofin", spinner_newState.getSelectedItem().toString());
                hashMap.put("blood", spinnerblood.getSelectedItem().toString());
                ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(AdminUserDetails.this, url, hashMap, USave);
                asyncTask.execute();
                //endregion
            }
        });

        btnuserbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region FindBooking of User
                String url=ServerCallAsyncTask.BASE_URL+"find_book_array";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("email", etemail.getText().toString());
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminUserDetails.this,url,hashMap,BSelect);
                asyncTask.execute();
                //endregion
            }
        });
        btnuserlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region FindUser Log
                String url=ServerCallAsyncTask.BASE_URL+"find_userlog_array";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("email", etemail.getText().toString());
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminUserDetails.this,url,hashMap,Ulog);
                asyncTask.execute();
                //endregion
            }
        });
    }

    private void fillFalse() {
        //region SetEnable False
        etFname.setEnabled(false);
        etLname.setEnabled(false);
        etemail.setEnabled(false);
        etpass.setEnabled(false);
        etdob.setEnabled(false);
        etMobile.setEnabled(false);
        etAddress.setEnabled(false);
        etpin.setEnabled(false);
        ettaluka.setEnabled(false);
        etdistrict.setEnabled(false);

        spinner_newState.setEnabled(false);
        spinnerblood.setEnabled(false);
        spinnergender.setEnabled(false);

        btnSave.setVisibility(View.INVISIBLE);
        btncancel.setVisibility(View.INVISIBLE);
        btnModify.setVisibility(View.VISIBLE);

        etFname.setTextColor(Color.BLACK);
        etLname.setTextColor(Color.BLACK);
        etemail.setTextColor(Color.BLACK);
        etpass.setTextColor(Color.BLACK);
        etdob.setTextColor(Color.BLACK);
        etMobile.setTextColor(Color.BLACK);
        etAddress.setTextColor(Color.BLACK);
        etpin.setTextColor(Color.BLACK);
        ettaluka.setTextColor(Color.BLACK);
        etdistrict.setTextColor(Color.BLACK);
        //endregion
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

    private void filluser() {
        String email = getIntent().getStringExtra("email");
        Doctor doctor = (Doctor) getIntent().getSerializableExtra("UserData");

        String url = ServerCallAsyncTask.BASE_URL + "patient";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if(DoctorConstants.AdminBookUser.equals("AdminBookUser")) {
            hashMap.put("email", email);
        } else {
            hashMap.put("email", doctor.getFirstName());
        }
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(this, url, hashMap, UPDATE);
        asyncTask.execute();
    }

    private void fillModifyuser() {
        String url = ServerCallAsyncTask.BASE_URL + "patient";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("email", etemail.getText().toString());
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(this, url, hashMap, UPDATE);
        asyncTask.execute();
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==UPDATE) {
            //region UserFill
            if (response.length() > 0) {
                SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Putpatient", response);
                editor.apply();

                String data = preferences.getString("Putpatient", "");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    jsonObject = jsonArray.getJSONObject(0);
                    etFname.setText(jsonObject.getString("fname"));
                    etLname.setText(jsonObject.getString("lname"));
                    etemail.setText(jsonObject.getString("email"));
                    etpass.setText(jsonObject.getString("ppass"));
                    etdob.setText(jsonObject.getString("dob"));
                    etMobile.setText(jsonObject.getString("mobileno"));
                    etAddress.setText(jsonObject.getString("address"));
                    etpin.setText(jsonObject.getString("pin_code"));
                    ettaluka.setText(jsonObject.getString("taluka"));
                    etdistrict.setText(jsonObject.getString("district"));
                    id=jsonObject.getString("id");

                    //region Set Spinner Of Blood_Group

                    String blood = jsonObject.getString("blood_grp");
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.blood_grp, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerblood.setAdapter(adapter1);
                    if (!blood.equals(null)) {
                        int spinnerPosition = adapter1.getPosition(blood);
                        spinnerblood.setSelection(spinnerPosition);
                    }

                    //endregion

                    //region Set Spinner Of gender

                    String gender = jsonObject.getString("gender");
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnergender.setAdapter(adapter2);
                    if (!gender.equals(null)) {
                        int spinnerPosition = adapter2.getPosition(gender);
                        spinnergender.setSelection(spinnerPosition);
                    }

                    //endregion

                    //region Set Spinner Of state

                    String state = jsonObject.getString("state");
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner_newState.setAdapter(adapter3);
                    if (!state.equals(null)) {
                        int spinnerPosition = adapter3.getPosition(state);
                        spinner_newState.setSelection(spinnerPosition);
                    }

                    //endregion

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid Booking!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if(flag==BSelect) {
            //region All Booking Of Users
            if(response.length()>0) {
                DoctorConstants.AdminUserBook="AdminUserBook";
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutUserBook",response);
                editor.apply();

                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        } else if (flag == UDelete) {
            //region User Delete
            if (response.trim().equals("1")) {
                Toast.makeText(this, "User Successfully Deleted.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AdminDash.class));
            } else {
                Toast.makeText(this, "Invalid Delete!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if (flag == USave) {
            //region Doctor Save
            if (response.trim().equals("1")) {
                Toast.makeText(this, "User Detail Successfully Saved.", Toast.LENGTH_SHORT).show();
                fillFalse();
                fillModifyuser();
            } else {
                Toast.makeText(this, "Invalid Save Details For User!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if(flag==Ulog) {
            //region All Log Of Users
            if(response.length()>0) {
                DoctorConstants.AdminLogSelect="AdminLogSelect";
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutUserLog",response);
                editor.apply();

                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        }
    }
}
