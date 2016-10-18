package com.example.ahmad.finalyear;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmad on 5/11/2016.
 */
public class ChangePassword extends Activity {
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/change_password.php";
    public String json=null, teacher_id, teacher_name,teacher_pwd,old_password,new_password,confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        final EditText old_pwd= (EditText) findViewById(R.id.old_pwd);
        final EditText new_pwd= (EditText) findViewById(R.id.new_pwd);
        final EditText confirm_pwd= (EditText) findViewById(R.id.confirm_pwd);
        TextView id= (TextView) findViewById(R.id.username);
        Button  change_pwd=(Button) findViewById(R.id.change_pwd);
        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            teacher_id = bn.getString("tch_id");
            teacher_name = bn.getString("tch_name");
            teacher_pwd = bn.getString("tch_password");

            Log.e("tcd_id", teacher_id);
            Log.e("tcd_name", teacher_name);
            Log.e("tcd_name", teacher_pwd);
        }
        id.setText(teacher_id);
        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                old_password = old_pwd.getText().toString();
                new_password = new_pwd.getText().toString();
                confirm_password = confirm_pwd.getText().toString();
                Log.e("old_pwd", old_password);

                if (old_pwd.getText().length() == 0) {
                    old_pwd.setError("Give Old Password");
                }
                if (new_pwd.getText().length() == 0) {
                    new_pwd.setError("Give New password");
                }
                if (confirm_pwd.getText().length() == 0) {
                    confirm_pwd.setError("Cofirm new password");
                }

                if (old_pwd.length() != 0 && new_pwd.length() != 0 && confirm_pwd.length() != 0) {
                    if (teacher_pwd.equals(old_password)) {
                        if (new_password.equals(confirm_password)) {

                            try{
                                if(isNetworkAvailable()) {
                                    new Changepwd().execute(teacher_id, new_password);
                                }
                                 else
                                {
                                    Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                                }
                            }catch(Exception e){
                                Log.e("err", e.toString());

                            }
                        }
                        else {
                            confirm_pwd.setError("Password does Not Match");
                            Toast.makeText(getApplicationContext(), "New Password does Not Match", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        old_pwd.setError("Old password incorrect");
                        Toast.makeText(getApplicationContext(), "Invalid Old Password", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }

    private class Changepwd extends AsyncTask<String, Void, Void> {
        private ProgressDialog pdia=new ProgressDialog(ChangePassword.this);

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

                    } else {
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
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }
            else {

                    pdia.dismiss();
                    Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_LONG).show();
                    finish();

            }

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
