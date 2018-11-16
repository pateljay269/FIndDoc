package com.example.jay.finddoc;

import android.content.*;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.*;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.*;
import android.support.v7.app.*;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DrCategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ServerCallAsyncTask.OnAsyncJSONResponse {

    private static final int Select = 1;
    RecyclerView recyclerView;
    MaterialEditText etdname;
    Toolbar toolbar;
    private ArrayList<Doctor> doctorArrayList, tempArrayList;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_category);

        recyclerView=(RecyclerView)findViewById(R.id.rvdrcat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        etdname = (MaterialEditText) findViewById(R.id.medtdnamewisecat);

        toolbar = (Toolbar) findViewById(R.id.drcattoolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.drcatfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(DrCategoryActivity.this, "Sending Mail", Toast.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drcatdrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.drcatnav_view);

        navigationView.setNavigationItemSelectedListener(this);

        drawer.openDrawer(GravityCompat.START);

        etdname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dName=etdname.getText().toString();
                if(dName.isEmpty()) {
                    doctorArrayList=tempArrayList;
                } else {
                    doctorArrayList = new ArrayList<Doctor>();
                    for (Doctor doctor :
                            tempArrayList) {
                        if(doctor.getFirstName().toLowerCase().contains(dName)
                                || doctor.getLastName().toLowerCase().contains(dName)){
                            doctorArrayList.add(doctor);
                        }
                    }
                }
                adapter=new DoctorAdapter(doctorArrayList,DrCategoryActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drcatdrawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String url=ServerCallAsyncTask.BASE_URL+"finddoctor_category";
        HashMap<String,String> hashMap=new HashMap<String, String>();

        //region Switch Case
        switch (id) {
            case R.id.nav_allergist:
                setTitle(R.string.nav_allergist);
                hashMap.put("drcat", "Allergist");
                break;

            case R.id.nav_audio:
                setTitle(R.string.nav_audiologist);
                hashMap.put("drcat", "Audiologist");
                break;

            case R.id.nav_cardio:
                setTitle(R.string.nav_cardiologist);
                hashMap.put("drcat", "Cardiologist");
                break;

            case R.id.nav_dentist:
                setTitle(R.string.nav_dentist);
                hashMap.put("drcat", "Dentist");
                break;

            case R.id.nav_dermatologist:
                setTitle(R.string.nav_dermatologist);
                hashMap.put("drcat", "Dermatologist");
                break;

            case R.id.nav_gyno:
                setTitle(R.string.nav_gynecologist);
                hashMap.put("drcat", "Gynecologist");
                break;

            case R.id.nav_neurologist:
                setTitle(R.string.nav_neurologist);
                hashMap.put("drcat", "Neurologist");
                break;

            case R.id.nav_neurosurgon:
                setTitle(R.string.nav_neurosurgon);
                hashMap.put("drcat", "Neurosurgon");
                break;

            case R.id.nav_ortho:
                setTitle(R.string.nav_orthopedic);
                hashMap.put("drcat", "Orthopedic");
                break;

            case R.id.nav_radiologist:
                setTitle(R.string.nav_radiologist);
                hashMap.put("drcat", "Radiologist");
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drcatdrawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void asyncGetSMSResponse(String response, int flag) {

        if(flag==Select) {
            //region DoctorCat
            if (response.length() > 0) {
                SharedPreferences preferences = getSharedPreferences(DoctorConstants.PREF_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PutCat", response);
                editor.apply();

                String data=preferences.getString("PutCat","");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocCat");
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

                    adapter=new DoctorAdapter(doctorArrayList,DrCategoryActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_LONG).show();
            }
            //endregion
        }
    }
}
