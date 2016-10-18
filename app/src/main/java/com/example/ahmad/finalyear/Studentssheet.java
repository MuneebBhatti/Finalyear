package com.example.ahmad.finalyear;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmad on 5/4/2016.
 */
public class Studentssheet extends Activity {
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/studentsheet.php";
    String  json,teacher_name,course_name,section_id,section_name,dt,get_message,lec_name;
    List<String> std_id = new ArrayList<String>();
    List<String> std_name = new ArrayList<String>();
    String[] students_name,students_id,status;
    TextView date,course_view,section_view;
    Button updtae_attd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.students_sheet);
        course_view=(TextView )findViewById(R.id.course_view);
        section_view=(TextView )findViewById(R.id.section_view);
        date = (TextView) findViewById(R.id.date);
        updtae_attd= (Button) findViewById(R.id.update_attendence);
        Bundle bn = getIntent().getExtras();
        if (bn != null) {
            teacher_name = bn.getString("tch_name");
            course_name= bn.getString("cr_name");
            section_id = bn.getString("sec_id");
            section_name=bn.getString("sec_name");

            Log.e("sec_id", section_id);
            Log.e("sec_name", section_name);
        }
        course_view.setText(course_name);
        section_view.setText("["+section_name+"]");
        Date d = new Date();
        CharSequence S  = DateFormat.format("yyyy-MM-d", d.getTime());
        dt=S.toString();
        date.setText(dt.toString());

        try {

            new StudentSheet().execute(section_id.toString());

        } catch (Exception e) {
            Log.e("err", e.toString());

        }
    }



    private class StudentSheet extends AsyncTask<String, Void, Void> {
        public ProgressDialog pdia = new ProgressDialog(Studentssheet.this);
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

            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sec_id", sec_id));


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
                        get_message=jsonObj.getString("message");
                                   lec_name = jsonObj.getString("lec_name");
                        JSONArray studentid = jsonObj.getJSONArray("student_id");
                        JSONArray studentname = jsonObj.getJSONArray("student_name");
                        Log.e("lec_name",lec_name);
                        //for geating array in string format...............
                        for (int i = 0; i < studentid.length(); i++) {
                            std_id.add(studentid.getString(i));
                            std_name.add(studentname.getString(i));

                        }
                        students_id= new String[ std_id.size() ];
                        students_id = std_id.toArray(students_id);
                        students_name= new String[ std_name.size() ];
                        students_name = std_name.toArray(students_name);

                        for (int i = 0; i < studentid.length(); i++) {

                            Log.e("student id", students_id[i]);
                            Log.e("student name", students_name[i]);

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
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
            else {
                if (validid == false) {

                    pdia.dismiss();
            if (get_message.equals("Succssed")) {
                ListView lv = (ListView) findViewById(R.id.sectios);
                final CustomAdapter obj = new CustomAdapter(Studentssheet.this, students_name, students_id, section_id, dt.toString());
                lv.setAdapter(obj);
                updtae_attd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkAvailable()) {
                            obj.myresult(lec_name);
                            Toast.makeText(getApplicationContext(), "Attendance updated", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else if (get_message.equals("TimeOver"))
            {
                updtae_attd.setBackgroundColor(Color.RED);
                updtae_attd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Time Over", Toast.LENGTH_LONG).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "Time Over", Toast.LENGTH_LONG).show();
            }
            else if (get_message.equals("NotExist"))
            {
                updtae_attd.setBackgroundColor(Color.RED);
                updtae_attd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Not class exist", Toast.LENGTH_LONG).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "Not class exist", Toast.LENGTH_LONG).show();
            }
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
