package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    private static final int UPDATEpass = 1;
    Button btnchangepass;
    EditText etoldpass,etnewpass,etconnewpass;
    String id,dbpass,conpass,pass,oldpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //region Declre IDs
        setTitle("Change Password");
        etoldpass=(EditText) findViewById(R.id.etoldpass);
        etnewpass=(EditText) findViewById(R.id.etnewpass);
        etconnewpass=(EditText) findViewById(R.id.etnewconfirmpass);
        btnchangepass=(Button) findViewById(R.id.btnChangePass);


        //endregion

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass=etnewpass.getText().toString();
                conpass=etconnewpass.getText().toString();
                oldpass=etoldpass.getText().toString();

                //region Check Correct Password
                if(oldpass.equals(dbpass)) {
                    //region Check Different Password
                    if (!oldpass.equals(pass)) {

                        //region Register Click Event
                        if (pass.equals(conpass)) {

                            //region Update Password
                            String url = ServerCallAsyncTask.BASE_URL + "update_pass";
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", id);
                            hashMap.put("pass", etnewpass.getText().toString());
                            ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(ChangePasswordActivity.this, url, hashMap, UPDATEpass);
                            asyncTask.execute();

                            //endregion
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Please Enter Correct Confirm Password ", Toast.LENGTH_LONG).show();
                        }

                        //endregion
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Please Enter Different New Password ", Toast.LENGTH_LONG).show();
                    }
                    //endregion
                }else
                {
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter Correct Password ", Toast.LENGTH_LONG).show();
                }
                //endregion
            }
        });

        fillDetail();
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
        if(flag==UPDATEpass)
        {
            if(response.trim().equals(UPDATEpass)) {
                startActivity(new Intent(getApplicationContext(), PatientDashActivity.class));
            }else
            {
                Toast.makeText(this, "Change Password Failed : "+response, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fillDetail() {
        SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
        String data=preferences.getString("USER","");
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            jsonObject=jsonArray.getJSONObject(0);

            dbpass=jsonObject.getString("ppass");

            id=jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
