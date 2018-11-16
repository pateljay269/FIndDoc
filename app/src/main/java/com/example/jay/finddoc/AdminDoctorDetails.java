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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminDoctorDetails extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse {

    MaterialEditText etdob,etFname,etLname,etlat,etlong,etemail,etAddress,ettaluka,etdist;
    Spinner special_spinner,gender_spinner,state_spinner;
    Button btnSave, btnModify, btndelete, btncancel,btnbook;
    String id;
    private static final int DSelect = 1,DBSelect=2,DDelete=3,DSave=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doctor_details);

        //region Declare IDs
        setTitle("Doctor Info");
        etFname= (MaterialEditText) findViewById(R.id.etADfname);
        etLname= (MaterialEditText) findViewById(R.id.etADlname);
        etemail= (MaterialEditText) findViewById(R.id.etADemail);
        etdob= (MaterialEditText) findViewById(R.id.etADdob);
        etlat= (MaterialEditText) findViewById(R.id.etADLat);
        etlong= (MaterialEditText) findViewById(R.id.etADLong);
        etAddress= (MaterialEditText) findViewById(R.id.etADaddress);
        ettaluka= (MaterialEditText) findViewById(R.id.etADTaluka);
        etdist= (MaterialEditText) findViewById(R.id.etADDistrict);

        state_spinner= (Spinner) findViewById(R.id.spinner_ADstate);
        special_spinner= (Spinner) findViewById(R.id.spinner_ADspecial);
        gender_spinner= (Spinner) findViewById(R.id.spinner_ADgender);

        btnSave = (Button) findViewById(R.id.btnADRowSave);
        btnbook = (Button) findViewById(R.id.btnADRowbook);
        btnModify = (Button) findViewById(R.id.btnADRowModify);
        btndelete = (Button) findViewById(R.id.btnADRowDel);
        btncancel = (Button) findViewById(R.id.btnADRowCancel);

        fillFalse();
        fillDoc();
        //endregion

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SetEnable True
                etFname.setEnabled(true);
                etLname.setEnabled(true);
                etemail.setEnabled(true);
                etdob.setEnabled(true);
                etlat.setEnabled(true);
                etlong.setEnabled(true);
                etAddress.setEnabled(true);
                ettaluka.setEnabled(true);
                etdist.setEnabled(true);

                state_spinner.setEnabled(true);
                special_spinner.setEnabled(true);
                gender_spinner.setEnabled(true);

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
                fillDoc();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SaveChanges
                String url = ServerCallAsyncTask.BASE_URL + "update_doctor";
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", id);
                hashMap.put("fname", etFname.getText().toString());
                hashMap.put("lname", etLname.getText().toString());
                hashMap.put("email", etemail.getText().toString());
                hashMap.put("dob", etdob.getText().toString());
                hashMap.put("lat", etlat.getText().toString());
                hashMap.put("long", etlong.getText().toString());
                hashMap.put("gender", gender_spinner.getSelectedItem().toString());
                hashMap.put("address", etAddress.getText().toString());
                hashMap.put("taluka", ettaluka.getText().toString());
                hashMap.put("district", etdist.getText().toString());
                hashMap.put("stateofin", state_spinner.getSelectedItem().toString());
                hashMap.put("special", special_spinner.getSelectedItem().toString());
                ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(AdminDoctorDetails.this, url, hashMap, DSave);
                asyncTask.execute();
                //endregion
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region DeleteDoc
                fillFalse();
                String url = ServerCallAsyncTask.BASE_URL + "deletedoc";
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email", etemail.getText().toString());
                ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(AdminDoctorDetails.this, url, hashMap, DDelete);
                asyncTask.execute();
                //endregion
            }
        });

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region FindBooking of Doctor
                String url=ServerCallAsyncTask.BASE_URL+"find_Docbook_array";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("email", etemail.getText().toString());
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminDoctorDetails.this,url,hashMap,DBSelect);
                asyncTask.execute();
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

    private void fillFalse() {
        //region SetEnable False
        etFname.setEnabled(false);
        etLname.setEnabled(false);
        etemail.setEnabled(false);
        etdob.setEnabled(false);
        etlat.setEnabled(false);
        etlong.setEnabled(false);
        etAddress.setEnabled(false);
        ettaluka.setEnabled(false);
        etdist.setEnabled(false);

        state_spinner.setEnabled(false);
        special_spinner.setEnabled(false);
        gender_spinner.setEnabled(false);

        btnSave.setVisibility(View.INVISIBLE);
        btncancel.setVisibility(View.INVISIBLE);
        btnModify.setVisibility(View.VISIBLE);

        etFname.setTextColor(Color.BLACK);
        etLname.setTextColor(Color.BLACK);
        etemail.setTextColor(Color.BLACK);
        etdob.setTextColor(Color.BLACK);
        etlat.setTextColor(Color.BLACK);
        etlong.setTextColor(Color.BLACK);
        etAddress.setTextColor(Color.BLACK);
        ettaluka.setTextColor(Color.BLACK);
        etdist.setTextColor(Color.BLACK);
        //endregion
    }
    private void fillDoc() {
        Doctor doctor = (Doctor) getIntent().getSerializableExtra("1DOCTOR");
        String url = ServerCallAsyncTask.BASE_URL + "finddocname";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("demail", doctor.getdEmail());
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(this, url, hashMap, DSelect);
        asyncTask.execute();
    }
    private void fillDocUpdate() {
        String url = ServerCallAsyncTask.BASE_URL + "finddocname";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("demail", etemail.getText().toString());
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(this, url, hashMap, DSelect);
        asyncTask.execute();
    }

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
                    id=jsonObject.getString("did");
                    etFname.setText(jsonObject.getString("fname"));
                    etLname.setText(jsonObject.getString("lname"));
                    etemail.setText(jsonObject.getString("d_email"));
                    etdob.setText(jsonObject.getString("dob"));
                    etlat.setText(jsonObject.getString("latitude"));
                    etlong.setText(jsonObject.getString("longitude"));
                    etAddress.setText(jsonObject.getString("address"));
                    ettaluka.setText(jsonObject.getString("taluka"));
                    etdist.setText(jsonObject.getString("district"));

                    //region Set Spinner Of Specialist
                    String blood = jsonObject.getString("specialist");
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.special_grp, android.R.layout.simple_spinner_item);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    special_spinner.setAdapter(adapter1);
                    if (!blood.equals(null)) {
                        int spinnerPosition = adapter1.getPosition(blood);
                        special_spinner.setSelection(spinnerPosition);
                    }
                    //endregion

                    //region Set Spinner Of gender
                    String gender = jsonObject.getString("gender");
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    gender_spinner.setAdapter(adapter2);
                    if (!gender.equals(null)) {
                        int spinnerPosition = adapter2.getPosition(gender);
                        gender_spinner.setSelection(spinnerPosition);

                    }
                    //endregion

                    //region Set Spinner Of state
                    String state = jsonObject.getString("state");
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    state_spinner.setAdapter(adapter3);
                    if (!state.equals(null)) {
                        int spinnerPosition = adapter3.getPosition(state);
                        state_spinner.setSelection(spinnerPosition);
                    }
                    //endregion

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid Doctor Details!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if (flag == DDelete) {
            //region Doctor Delete
            if (response.trim().equals("1")) {
                Toast.makeText(this, "Your Doctor Successfully Deleted.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AdminDash.class));
            } else {
                Toast.makeText(this, "Invalid Delete!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if (flag == DSave) {
            //region Doctor Save
            if (response.trim().equals("1")) {
                Toast.makeText(this, "Your Doctor Detail Successfully Saved.", Toast.LENGTH_SHORT).show();
                fillFalse();
                fillDocUpdate();
            } else {
                Toast.makeText(this, "Invalid Save Details For Doctor!!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        } else if(flag==DBSelect) {
            //region All Booking Of Doctor
            if(response.length()>0) {
                DoctorConstants.AdminDoc="2";
                DoctorConstants.AdminDocBooking="AdminDocBooking";
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutAdminDocBook",response);
                editor.apply();

                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        }
    }
}
