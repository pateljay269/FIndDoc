package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyprofileActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    Button btnMyCal,btnreport,btnsearchAppoint;
    private static final int Select = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        //region Declare IDs
        setTitle("My Profile");
        btnMyCal=(Button) findViewById(R.id.btnmybooking);
        btnreport=(Button) findViewById(R.id.btnMycalender);
        btnsearchAppoint = (Button) findViewById(R.id.btnsearchappointment);
        //endregion

        btnMyCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //region Select From BookAppointment
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                String data=preferences.getString("USER","");
                try {
                    JSONObject jsonObject=new JSONObject(data);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    jsonObject=jsonArray.getJSONObject(0);
                    String url=ServerCallAsyncTask.BASE_URL+"find_book_array";
                    HashMap<String,String> hashMap=new HashMap<String, String>();
                    hashMap.put("email",jsonObject.getString("email"));
                    ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(MyprofileActivity.this,url,hashMap,Select);
                    asyncTask.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
            }
        });

        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnsearchAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SearchAppointActivity.class));
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

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==Select)
        {
            if(response.length()>0)
            {
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutBook",response);
                editor.apply();
                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}