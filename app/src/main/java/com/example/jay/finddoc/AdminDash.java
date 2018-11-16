package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.HashMap;

public class AdminDash extends AppCompatActivity implements ServerCallAsyncTask.OnAsyncJSONResponse{

    ImageButton btnadddoc,btnusers,btnbookings,btnlog,btnaddpat,btndoc;
    private static final int BSelect = 1,USelect=2,DSelect=3,LogSelect=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        //region Declare IDs
        setTitle("Admin Profile");
        btnadddoc=(ImageButton) findViewById(R.id.btnadminRDoc);
        btnaddpat=(ImageButton) findViewById(R.id.btnadminRpat);
        btnusers=(ImageButton) findViewById(R.id.btnadminuser);
        btndoc=(ImageButton) findViewById(R.id.btnadmindoc);
        btnbookings=(ImageButton) findViewById(R.id.btnadminbookings);
        btnlog=(ImageButton) findViewById(R.id.btnadminlog);
        DoctorConstants.AdminUser="1";
        DoctorConstants.AdminDoc="1";
        DoctorConstants.AdminBook="1";
        DoctorConstants.AdminUserBook="1";
        DoctorConstants.AdminBookUser="1";
        DoctorConstants.AdminLogSelect="1";
        //endregion

        btnadddoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterDocActivity.class));
            }
        });

        btnusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ServerCallAsyncTask.BASE_URL+"find_admin_user";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminDash.this,url,hashMap,USelect);
                asyncTask.execute();
            }
        });

        btndoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ServerCallAsyncTask.BASE_URL+"find_admin_doc";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminDash.this,url,hashMap,DSelect);
                asyncTask.execute();
            }
        });

        btnbookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ServerCallAsyncTask.BASE_URL+"find_admin_book";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminDash.this,url,hashMap,BSelect);
                asyncTask.execute();
            }
        });

        btnaddpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=ServerCallAsyncTask.BASE_URL+"find_admin_userlog";
                HashMap<String,String> hashMap=new HashMap<String, String>();
                ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(AdminDash.this,url,hashMap,LogSelect);
                asyncTask.execute();
            }
        });
    }

    //region Home_Action_Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homeonly, menu);
        DoctorConstants.mainMenu=menu;
        super.onCreateOptionsMenu(menu);
        menu.getItem(0).setIcon(
                R.drawable.logout);
        return true; }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            //For LogOut
            SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("USER","");
            editor.apply();
            startActivity(new Intent(getApplicationContext(),HomescreenActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if(flag==USelect) {
            //region AdminUser
            if(response.length()>0) {
                DoctorConstants.AdminUser="AdminUser";
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutAdminUser",response);
                editor.apply();

                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        } else if(flag==DSelect) {
            //region AdminDoc
            if(response.length()>0) {
                DoctorConstants.AdminDoc="AdminDoc";
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutAdminDoc",response);
                editor.apply();

                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        } else if(flag==BSelect) {
            //region AdminBook
            if(response.length()>0) {
                DoctorConstants.AdminBook="AdminBook";
                SharedPreferences preferences=getSharedPreferences(DoctorConstants.PREF_FILE,MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("PutAdminBook",response);
                editor.apply();
                startActivity(new Intent(getApplicationContext(),MyCalender.class));
            }else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
            //endregion
        } else if(flag==LogSelect) {
            //region AdminLog
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
