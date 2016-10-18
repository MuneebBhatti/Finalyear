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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/signin.php";
    String json,teacher_id,teacher_name,teacher_pwd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText username= (EditText) findViewById(R.id.username);
        final EditText password= (EditText) findViewById(R.id.password);
        Button login= (Button)findViewById(R.id.login);
        Button forgot= (Button)findViewById(R.id.forgot);
        //adding listener...............
        login.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        if(username.length() == 0 )
        {username.setError("Give your username");}
        if(password.length() == 0)
        {password.setError("Give your password");}


        Log.v("EditText", username.getText().toString());
        Log.v("EditText", password.getText().toString());

        if(username.length() != 0 && password.length() != 0) {
            try{
                 if(isNetworkAvailable())
                 {
                        new CheckUser().execute(username.getText().toString(), password.getText().toString());
                 }
                 else
                 {
                     Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                 }

            }catch(Exception e){
                Log.e("err", e.toString());

            }
        }

  }
            });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot= new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(forgot);
            }
        });
    }

    private class CheckUser extends AsyncTask<String, Void, Void> {
        private ProgressDialog pdia=new ProgressDialog(MainActivity.this);
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

            String uname = arg[0];
            String pword = arg[1];


            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uname", uname));
            params.add(new BasicNameValuePair("pword", pword));


            Servicehandler serviceClient = new Servicehandler();

            json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
                    Servicehandler.POST, params);


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
                        teacher_id = jsonObj.getString("teacher");
                        teacher_name=jsonObj.getString("teacher_name");
                        teacher_pwd=jsonObj.getString("teacher_password");

                        Log.e("name", teacher_name);
                        Log.e("name", teacher_id );
                        Log.e("name", teacher_pwd );

                    } else {
                        validid=true;
                        Log.e(" Error: ",
                                "> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

                else{

                            Log.e("json","Error in json");
                        }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(json == null){
                pdia.dismiss();
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
            }
            else
            {
                if(validid==false){
                            pdia.dismiss();
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            i.putExtra("tch_id", teacher_id);
                            i.putExtra("tch_name", teacher_name);
                            i.putExtra("tch_password", teacher_pwd);
                            startActivity(i);
                            finish();
                }
                else
                {
                    pdia.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid Username Password", Toast.LENGTH_LONG).show();
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
