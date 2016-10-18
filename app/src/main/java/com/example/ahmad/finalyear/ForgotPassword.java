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
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmad on 7/22/2016.
 */
public class ForgotPassword extends Activity {
    private String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/forgotpwd.php";
    String json,message,emailid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        final EditText username= (EditText) findViewById(R.id.username);
        final EditText email=(EditText) findViewById(R.id.email);
        Button send=(Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.length() == 0 )
                {username.setError("Give your username");}
                if (email.length() == 0) {
                    email.setError("Give your phonenumber");
                }
                 emailid=email.getText().toString();
                if (username.length() != 0 && email.length() != 0) {
                    try {
                        if(isNetworkAvailable())
                        {
                            new RecoverPassword().execute(username.getText().toString(), emailid);
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
    private class RecoverPassword extends AsyncTask<String, Void, Void> {
        private ProgressDialog pdia=new ProgressDialog(ForgotPassword.this);
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
            String email = arg[1];


            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uname", uname));
            params.add(new BasicNameValuePair("email", email));

            Log.e("uname ", uname);
            Log.e("Email ", email);


            Servicehandler serviceClient = new Servicehandler();

            json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
                    Servicehandler.GET, params);
            Log.e("uname ", uname);
            Log.e("Email ",email);

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
                        message = jsonObj.getString("message");

                        Log.e("message", message);

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
            }else {
                if(validid==false){
                        pdia.dismiss();
                        Toast.makeText(getApplicationContext(), "Password sent to your email id", Toast.LENGTH_SHORT).show();
                }
                else {
                    pdia.dismiss();
                    Toast.makeText(getApplicationContext(), "Username or email not exist", Toast.LENGTH_SHORT).show();
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




