package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FindDocPlace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ServerCallAsyncTask.OnAsyncJSONResponse {

    //region Define
    private static final int Select = 1;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView recyclerView;
    MaterialEditText etdname;
    Toolbar toolbar;
    private ArrayList<Doctor> doctorArrayList, tempArrayList;
    private DoctorAdapter adapter;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doc_place);


        recyclerView=(RecyclerView)findViewById(R.id.rvdrarea);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        etdname = (MaterialEditText) findViewById(R.id.medtdnamewiseArea);

        //region Default for navigation drawer
        toolbar = (Toolbar) findViewById(R.id.drareatoolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.drareafab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drareadrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.drareanav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.openDrawer(GravityCompat.START);

        //endregion

        etdname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dName=etdname.getText().toString();
                if(dName.isEmpty())
                {
                    doctorArrayList=tempArrayList;
                }else {
                    doctorArrayList = new ArrayList<Doctor>();
                    for (Doctor doctor :
                            tempArrayList) {
                        if(doctor.getFirstName().toLowerCase().contains(dName)
                                || doctor.getLastName().toLowerCase().contains(dName)){
                            doctorArrayList.add(doctor);
                        }
                    }
                }
                adapter=new DoctorAdapter(doctorArrayList,FindDocPlace.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drareadrawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String url=ServerCallAsyncTask.BASE_URL+"DocAreaFind";
        HashMap<String,String> hashMap=new HashMap<String, String>();

        //region Switch Case
        switch (id) {
            case R.id.nav_adajan:
                setTitle(R.string.nav_adajan);
                hashMap.put("drarea", "Adajan");
                break;

            case R.id.nav_althan:
                setTitle(R.string.nav_althan);
                hashMap.put("drarea", "Althan");
                break;

            case R.id.nav_athwa:
                setTitle(R.string.nav_athwa);
                hashMap.put("drarea", "Athwalines");
                break;

            case R.id.nav_bhatar:
                setTitle(R.string.nav_bhatar);
                hashMap.put("drarea", "Bhatar");
                break;

            case R.id.nav_citylight:
                setTitle(R.string.nav_citylight);
                hashMap.put("drarea", "Citylight");
                break;

            case R.id.nav_delhigate:
                setTitle(R.string.nav_delhigate);
                hashMap.put("drarea", "Delhi Gate");
                break;

            case R.id.nav_katargam:
                setTitle(R.string.nav_katargam);
                hashMap.put("drarea", "Katargam");
                break;

            case R.id.nav_nanpura:
                setTitle(R.string.nav_nanpura);
                hashMap.put("drarea", "Nanpura");
                break;

            case R.id.nav_olpad:
                setTitle(R.string.nav_olpad);
                hashMap.put("drarea", "Olpad");
                break;

            case R.id.nav_pandesara:
                setTitle(R.string.nav_pandesara);
                hashMap.put("drarea", "Pandesara");
                break;

            case R.id.nav_udhna:
                setTitle(R.string.nav_udhna);
                hashMap.put("drarea", "Udhna");
                break;

            case R.id.nav_varacha:
                setTitle(R.string.nav_varacha);
                hashMap.put("drarea", "Varacha");
                break;

            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        //endregion

        ServerCallAsyncTask asyncTask=new ServerCallAsyncTask(this,url,hashMap,Select);
        asyncTask.execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drareadrawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        if(flag==Select) {
            if (response.length() > 0) {
                SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PutArea", response);
                editor.apply();

                String data=preferences.getString("PutArea","");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocArea");
                    doctorArrayList=new ArrayList<>();

                    for(int i=0;i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Doctor doctor=new Doctor();
                        doctor.setHead("Doctor: "+String.valueOf(i+1));
                        doctor.setFirstName(jsonObject.getString("fname"));
                        doctor.setLastName(jsonObject.getString("lname"));
                        doctor.setdEmail(jsonObject.getString("d_email"));
                        doctorArrayList.add(doctor);

                    }
                    tempArrayList=doctorArrayList;
                    adapter=new DoctorAdapter(doctorArrayList,FindDocPlace.this);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
