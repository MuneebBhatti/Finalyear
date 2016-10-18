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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmad on 7/26/2016.
 */
public class StudentSearch extends Activity {
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/searchstudent.php";
    public String json,cr_name,section_id,section_name,student_id,student_name;
    List<String> student_status = new ArrayList<String>();
    List<String> student_date = new ArrayList<String>();
    List<String> Lecture_name = new ArrayList<String>();
    String[] stdstatus,stddate,lecname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchstudent);
        final EditText std_id=(EditText) findViewById(R.id.std_id);
        Button search=(Button) findViewById(R.id.search);
        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            cr_name = bn.getString("cr_name");
            section_id = bn.getString("sec_id");
            section_name = bn.getString("sec_name");

        }
search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        student_id=std_id.getText().toString();
        if (std_id.length() == 0) {
            std_id.setError("Enter student id");
        }
        if (std_id.length() != 0) {
            try {
                if(isNetworkAvailable())
                {
                    new Searchstudent().execute(section_id, student_id);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.e("err", e.toString());
            }
        }
    }
});

    }



    private class Searchstudent extends AsyncTask<String, Void, Void> {
        private ProgressDialog pdia = new ProgressDialog(StudentSearch.this);
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

            String sec_id = arg[0];
            String std_id = arg[1];

                Log.e("section id", sec_id);
                Log.e("student id", std_id);

            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sec_id",sec_id));
            params.add(new BasicNameValuePair("std_id",std_id));
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
                        student_name=jsonObj.getString("name");
                        JSONArray lec_name=jsonObj.getJSONArray("lec_name");
                        JSONArray statusid = jsonObj.getJSONArray("status");
                        JSONArray dateid = jsonObj.getJSONArray("date");
                        //for geating array in string format...............

                        for (int i = 0; i < statusid.length(); i++) {
                            Lecture_name.add(lec_name.getString(i));
                            student_status.add(statusid.getString(i));
                            student_date.add(dateid.getString(i));
                        }
                        lecname=new String[Lecture_name.size()];
                        lecname=Lecture_name.toArray(lecname);
                        stdstatus= new String[ student_status.size() ];
                        stdstatus = student_status.toArray(stdstatus);
                        stddate= new String[ student_date.size() ];
                        stddate = student_date.toArray(stddate);


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
                    Intent i= new Intent (StudentSearch.this,StudentAttendenceDetail.class);
                        i.putExtra("std_date",stddate);
                        i.putExtra("std_status",stdstatus);
                        i.putExtra("lec_name",lecname);
                        i.putExtra("sec_name",section_name);
                        i.putExtra("id",student_id.toUpperCase());
                        i.putExtra("name",student_name);
                        startActivity(i);
                        finish();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No student Found", Toast.LENGTH_LONG).show();
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








