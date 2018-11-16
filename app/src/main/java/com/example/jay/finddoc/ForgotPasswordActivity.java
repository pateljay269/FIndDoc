package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.*;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ForgotPasswordActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    final static int ForGET=1;
    Button btnsend;
    EditText etemail,etmob;
    TextView tvpass,tvpassample;
    String lemail,lmob,d_email,d_mob,id,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //region Declare IDs
        setTitle("Forgot Password");
        tvpass = (TextView) findViewById(R.id.tvforpass);
        tvpassample = (TextView) findViewById(R.id.tvforpass1);

        etemail=(EditText) findViewById(R.id.etforpassemail);
        etmob=(EditText) findViewById(R.id.etforpassmob);

        btnsend = (Button) findViewById(R.id.btnforpasssend);

        tvpassample.setVisibility(View.INVISIBLE);

        //endregion
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //region Validation
                lemail=etemail.getText().toString();
                lmob=etmob.getText().toString();

                if(!etemail.getText().toString().isEmpty() && !etmob.getText().toString().isEmpty() ) {

                    //region Check In Database
                    String url = ServerCallAsyncTask.BASE_URL + "forgot_password";
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("email", lemail);
                    hashMap.put("mob", lmob);
                    ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(ForgotPasswordActivity.this, url, hashMap, ForGET);
                    asyncTask.execute();
                    //endregion

                    //region Send Msg on Mobile
/*
                    String sms="",phoneNo = etmob.getText().toString(),message = "hi";
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS Faild"+e,
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    try {

                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("sms_body", "default content");
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        startActivity(sendIntent);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
*/
                    //endregion
                } else {

                    //region Check Email,MobileNo and Dob Is Empty Or Not
                    if(etemail.getText().toString().isEmpty()) {
                        etemail.setError("Enter Email");
                    }

                    if(TextUtils.isEmpty(etmob.getText().toString())) {
                        etmob.setError("Enter Mobile");
                    }

                    Toast.makeText(ForgotPasswordActivity.this, "Please Provide All Details", Toast.LENGTH_SHORT).show();
                    //endregion
                }
                //endregion
            }
        });
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if (flag == ForGET) {
            if (response.length() > 0) {
                SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("USER1", response);
                editor.apply();

                fillDetail();

                if (d_mob.equals(lmob) && d_email.equals(lemail) ) {
                    tvpassample.setVisibility(View.VISIBLE);
                    tvpass.setText(pass);
                } else {
                    preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("USER1","");
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(),HomescreenActivity.class));
                    Toast.makeText(this, "Re-Try ", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Invalid Details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fillDetail() {
        SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
        String data=preferences.getString("USER1","");
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            jsonObject=jsonArray.getJSONObject(0);

            d_mob=jsonObject.getString("mobileno");
            d_email=jsonObject.getString("email");
            pass=jsonObject.getString("ppass");

            id=jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
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
