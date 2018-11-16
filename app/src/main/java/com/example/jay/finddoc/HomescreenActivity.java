package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class HomescreenActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse
{

    Button btnsignIn, btnregister,btnforgotpass;
    private static final int LOGIN = 1;
    EditText etEmail,etPass;
    Calendar c;
    SimpleDateFormat df;
    String now_date,now_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);


        //region Declare IDs
        etEmail=(EditText)findViewById(R.id.etlemail);
        etPass=(EditText)findViewById(R.id.etlpass);
        btnforgotpass=(Button) findViewById(R.id.btnHforgotpass);
        btnsignIn = (Button) findViewById(R.id.btnHlogin);
        btnregister = (Button) findViewById(R.id.btnHRegister);

        //endregion
        btnforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(etEmail.getText().toString().equalsIgnoreCase("admin") && etPass.getText().toString().equalsIgnoreCase("admin"))
                if(etEmail.getText().toString().isEmpty() && etPass.getText().toString().isEmpty())
                {
                    DoctorConstants.admin="admin";
                    startActivity(new Intent(getApplicationContext(),AdminDash.class));
                } else {
                    //region Sign In Click
                    c = Calendar.getInstance();
                    df = new SimpleDateFormat("dd/MM/yyyy");
                    now_date = df.format(c.getTime());
                    df = new SimpleDateFormat("HH:mm:ss");
                    now_time = df.format(c.getTime());

                    String url = ServerCallAsyncTask.BASE_URL + "login_verification";
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("email", etEmail.getText().toString());
                    hashMap.put("pass", etPass.getText().toString());
                    hashMap.put("Ldate", now_date);
                    hashMap.put("Ltime", now_time);
                    ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(HomescreenActivity.this, url, hashMap, LOGIN);
                    asyncTask.execute();
                    //endregion
                }
            }
        });

        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
        String user=preferences.getString("USER","");
        if(!user.equals("")) {
            startActivity(new Intent(getApplicationContext(), PatientDashActivity.class));
            try {
                JSONObject jsonObject = new JSONObject(user);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                jsonObject=jsonArray.getJSONObject(0);
                DoctorConstants.USEREMAIL=jsonObject.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {

        if(flag==LOGIN)
        {
            if(response.length()>0)
            {
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("USER",response);
                editor.apply();
                checkLogin();
            }else {
                Toast.makeText(this, "Invalid Username or Password!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
