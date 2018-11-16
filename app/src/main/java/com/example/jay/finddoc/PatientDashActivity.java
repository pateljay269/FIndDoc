package com.example.jay.finddoc;

import android.content.*;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class PatientDashActivity extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    ImageButton btncat,btndfinddoc,btnprofile,btnnear;
    private static final int DSelect = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dash);

        //region Declare IDs
        setTitle("Patient Dashboard");
        DoctorConstants.admin="0";
        btncat=(ImageButton) findViewById(R.id.btncategory);
        btndfinddoc=(ImageButton) findViewById(R.id.btndfindoc);
        btnnear=(ImageButton) findViewById(R.id.btnnearme);
        btnprofile=(ImageButton) findViewById(R.id.btnmyprofile);

        //endregion

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyprofileActivity.class));
            }
        });

        btncat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DrCategoryActivity.class));
            }
        });

        btndfinddoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FindDocPlace.class));
            }
        });

        btnnear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),FindDoctorActivity.class));
                String url=ServerCallAsyncTask.BASE_URL+"find_admin_doc";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(PatientDashActivity.this,url,hashMap,DSelect);
                asyncTask.execute();
            }
        });
    }

    //region Master_Action_Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_changes, menu);
        DoctorConstants.mainMenu=menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_editprofile) {
            startActivity(new Intent(getApplicationContext(),ProfileDetailActivity.class));
        }
        else if (id == R.id.action_changepassword) {
            startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));
        }
        else if (id == R.id.action_logout) {
            //region For LogOut
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String now_date = df.format(c.getTime());
            df = new SimpleDateFormat("HH:mm:ss");
            String now_time = df.format(c.getTime());

            String url=ServerCallAsyncTask.BASE_URL+"logout";
            HashMap<String,String> hashMap=new HashMap<String, String>();
            hashMap.put("email", DoctorConstants.USEREMAIL);
            hashMap.put("Ldate", now_date);
            hashMap.put("Ltime", now_time);
            ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(PatientDashActivity.this,url,hashMap,DoctorConstants.LOGOUT);
            asyncTask.execute();
            //endregion
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==DoctorConstants.LOGOUT) {
            //region AdminUser
            if(response.trim().equals("1")) {
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("USER","");
                editor.apply();

                startActivity(new Intent(getApplicationContext(),HomescreenActivity.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        } else if(flag==DSelect) {
            //region AllDoc
            if(response.length()>0) {
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutDoc",response);
                editor.apply();

                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        }
    }


}
