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

/**
 * Created by Ahmad on 4/30/2016.
 */
public class SelectSection extends Activity{
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/select_section.php";
    String  json,teacher_id,teacher_name,course_name;
    List<String> sec_name = new ArrayList<String>();
    List<String> sec_id = new ArrayList<String>();
    String[] sections_name,sections_id;
    TextView tech_name,section_view;
    ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_section);
        tech_name = (TextView) findViewById(R.id.tech_name);
        section_view=(TextView )findViewById(R.id.section_view);
        listView = (ListView) findViewById(R.id.sectios);
        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            teacher_id = bn.getString("tch_id");
            teacher_name = bn.getString("tch_name");
            course_name= bn.getString("cr_name");

            Log.e("tcd_id", teacher_id);
        }
        tech_name.setText(teacher_name);
        section_view.setText(course_name);
        Log.e("tcd_name", teacher_name);
        Log.e("course_name", course_name);

        try {

            new SectionSelect().execute(teacher_id.toString(),course_name.toString());

        } catch (Exception e) {
            Log.e("err", e.toString());

        }
    }



    private class SectionSelect extends AsyncTask<String, Void, Void> {
        private ProgressDialog pdia = new ProgressDialog(SelectSection.this);
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
            String course_name = arg[1];


            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tech_id", tech_id));
            params.add(new BasicNameValuePair("course_name", course_name));


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
                        JSONArray sectionid = jsonObj.getJSONArray("section_id");
                        JSONArray sectionname = jsonObj.getJSONArray("section_name");
                        //for geating array in string format...............
                        for (int i = 0; i < sectionid.length(); i++) {
                            sec_id.add(sectionid.getString(i));
                            sec_name.add(sectionname.getString(i));

                        }
                        sections_id= new String[ sec_id.size() ];
                        sections_id = sec_id.toArray(sections_id);
                        sections_name= new String[ sec_name.size() ];
                        sections_name = sec_name.toArray(sections_name);

                        for (int i = 0; i < sectionname.length(); i++) {

                            Log.e("section id", sections_id [i]);
                            Log.e("section name", sections_name[i]);

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
        }else {
            if (validid == false) {

                pdia.dismiss();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectSection.this, android.R.layout.simple_list_item_1, sections_name);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        if (isNetworkAvailable())
                        {
                            Intent intent = new Intent(SelectSection.this, Studentssheet.class);
                            intent.putExtra("tch_name", teacher_name);
                            intent.putExtra("cr_name", course_name);
                            intent.putExtra("sec_id", sections_id[position]);
                            intent.putExtra("sec_name", sections_name[position]);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_SHORT).show();
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
