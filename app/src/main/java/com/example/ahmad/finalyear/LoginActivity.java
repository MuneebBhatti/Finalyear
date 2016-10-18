package com.example.ahmad.finalyear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ahmad on 5/11/2016.
 */
public class LoginActivity extends Activity {
    public String json=null, teacher_id, teacher_name,teacher_pwd;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button mark_attendence= (Button)findViewById(R.id.mark_attendence);
        Button view_attendence= (Button)findViewById(R.id.view_attendence);
        Button chande_pwd= (Button)findViewById(R.id.change_pwd);

        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            teacher_id = bn.getString("tch_id");
            teacher_name = bn.getString("tch_name");
            teacher_pwd = bn.getString("tch_password");

            Log.e("tcd_id", teacher_id);
            Log.e("tcd_name", teacher_name);
            Log.e("tcd_name", teacher_pwd);

        }
        mark_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {

                    Intent i = new Intent(LoginActivity.this, CourseSelect.class);
                    i.putExtra("tch_id", teacher_id);
                    i.putExtra("tch_name", teacher_name);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                     }
            }
        });
        view_attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    Intent i = new Intent(LoginActivity.this, ViewattendenceCourse.class);
                    i.putExtra("tch_id", teacher_id);
                    i.putExtra("tch_name", teacher_name);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
                }
            }
        });


        chande_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ChangePassword.class);
                i.putExtra("tch_id", teacher_id);
                i.putExtra("tch_name", teacher_name);
                i.putExtra("tch_password", teacher_pwd);
                startActivity(i);
            }
        });
            }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
