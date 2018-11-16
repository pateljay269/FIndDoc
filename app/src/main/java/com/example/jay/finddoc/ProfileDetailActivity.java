package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileDetailActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse {

    private static final int UPDATE = 1;
    Button btnsavechanges;
    MaterialEditText etnewFname,etnewLname,etnewMobile,etnewAddress,etnewpin,etnewtaluka,etnewdistrict,etemail,etdob;
    Spinner spinner_newState,spinnerblood,spinnergender;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        //region Declare IDs
        setTitle("Profile Details");
        btnsavechanges=(Button) findViewById(R.id.btnsavechange);
        etnewFname= (MaterialEditText) findViewById(R.id.etnewfname);
        etnewLname= (MaterialEditText) findViewById(R.id.etnewlname);
        etnewAddress= (MaterialEditText) findViewById(R.id.etnewaddress);
        etnewMobile= (MaterialEditText) findViewById(R.id.etnewmob);
        etnewpin= (MaterialEditText) findViewById(R.id.etnewpincode);
        etnewtaluka= (MaterialEditText) findViewById(R.id.etnewTaluka);
        etnewdistrict= (MaterialEditText) findViewById(R.id.etnewDistrict);
        etemail= (MaterialEditText) findViewById(R.id.etcurremail);
        etdob= (MaterialEditText) findViewById(R.id.etcurrdob);

        spinner_newState = (Spinner) findViewById(R.id.spinner_newstate);
        spinnerblood = (Spinner) findViewById(R.id.spinner_currblood);
        spinnergender = (Spinner) findViewById(R.id.spinner_currgender);

        //endregion

        btnsavechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //region Update Patient Details
                String url=ServerCallAsyncTask.BASE_URL+"update_patient";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("id",id);
                hashMap.put("fname",etnewFname.getText().toString());
                hashMap.put("lname",etnewLname.getText().toString());
                hashMap.put("email",etemail.getText().toString());
                hashMap.put("mobile",etnewMobile.getText().toString());
                hashMap.put("address",etnewAddress.getText().toString());
                hashMap.put("pincode",etnewpin.getText().toString());
                hashMap.put("taluka",etnewtaluka.getText().toString());
                hashMap.put("district",etnewdistrict.getText().toString());
                hashMap.put("stateofin", spinner_newState.getSelectedItem().toString());
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(ProfileDetailActivity.this,url,hashMap,UPDATE);
                asyncTask.execute();

                //endregion
            }
        });

        fillDetail();
    }

    private void fillDetail() {
        SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
        String data=preferences.getString("USER","");
        try {
            //region FillDetails
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            jsonObject=jsonArray.getJSONObject(0);
            etnewFname.setText(jsonObject.getString("fname"));
            etnewLname.setText(jsonObject.getString("lname"));
            etnewMobile.setText(jsonObject.getString("mobileno"));
            etnewAddress.setText(jsonObject.getString("address"));
            etnewpin.setText(jsonObject.getString("pin_code"));
            etnewtaluka.setText(jsonObject.getString("taluka"));
            etnewdistrict.setText(jsonObject.getString("district"));
            etemail.setText(jsonObject.getString("email"));
            etdob.setText(jsonObject.getString("dob"));

            spinnerblood.setEnabled(false);
            spinnergender.setEnabled(false);

            //region Set Spinner Of Blood_Group

            String blood = jsonObject.getString("blood_grp");
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.blood_grp, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_newState.setAdapter(adapter1);
            if (!blood.equals(null)) {
                int spinnerPosition = adapter1.getPosition(blood);
                spinnerblood.setSelection(spinnerPosition);
            }

            //endregion

            //region Set Spinner Of gender

            String gender = jsonObject.getString("gender");
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_newState.setAdapter(adapter2);
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

            id=jsonObject.getString("id");
            //endregion
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==UPDATE)
        {
            if(response.length()>0) {
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("USER",response);
                editor.apply();
                fillDetail();
                //startActivity(new Intent(getApplicationContext(), HomescreenActivity.class));
            }else
            {
                Toast.makeText(this, "Update Detail Failed : "+response, Toast.LENGTH_LONG).show();
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
