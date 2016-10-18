package com.example.ahmad.finalyear;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewattendenceCourse extends Activity {
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/select_course.php";
    public String json=null, teacher_id, teacher_name;
    List<String> cr_name = new ArrayList<String>();
    String[] courses_name;
    TextView tech_name,cr_select;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_course);
        tech_name = (TextView) findViewById(R.id.tech_name);
        cr_select = (TextView) findViewById(R.id.select_course);
        listView = (ListView) findViewById(R.id.courses_list);
        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            teacher_id = bn.getString("tch_id");
            teacher_name = bn.getString("tch_name");

            Log.e("tcd_id", teacher_id);
            Log.e("tcd_name", teacher_name);
        }
        cr_select.setText("Select to view attendence");
        tech_name.setText(teacher_name);


        try {

            new CrSelect().execute(teacher_id.toString());

        } catch (Exception e) {
            Log.e("err", e.toString());

        }

    }

    private class CrSelect extends AsyncTask<String, Void, Void> {
        private ProgressDialog pdia = new ProgressDialog(ViewattendenceCourse.this);
        Boolean validid=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia.setMessage("Please wait...");
            pdia.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub

            String tech_id = arg[0];


            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tech_id", tech_id));


            Servicehandler serviceClient = new Servicehandler();

            json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
                    Servicehandler.GET, params);


            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    Log.d("Login : ", "> " +jsonObj.toString());

                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        Log.e("Login successfully ",
                                "> " + jsonObj.getString("message"));
                        JSONArray coursename = jsonObj.getJSONArray("course_name");

                        //for geating array in string format...............
                        for (int i = 0; i < coursename.length(); i++) {

                                cr_name.add(coursename.getString(i));
                        }
                        courses_name = new String[ cr_name.size() ];

                        for (int i = 0; i < coursename.length();i++) {

                            courses_name = cr_name.toArray(courses_name);
                        }
                        for (int i = 0; i < coursename.length();i++) {

                            Log.e("name", courses_name[i]);

                        }


                    } else {
                        validid=true;
                        Log.e(" Error: ",
                                "> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                Log.e("json", "Error in json");
            }

            pdia.dismiss();
            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(json == null){
                pdia.dismiss();
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
            }else {
                if (validid == false) {

                    pdia.dismiss();

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewattendenceCourse.this, android.R.layout.simple_list_item_1, courses_name);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            if(isNetworkAvailable()) {
                                Intent intent = new Intent(ViewattendenceCourse.this, ViewattendenceSection.class);
                                intent.putExtra("tch_id", teacher_id);
                                intent.putExtra("tch_name", teacher_name);
                                intent.putExtra("cr_name", courses_name[position]);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    }

