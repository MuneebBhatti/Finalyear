package com.example.ahmad.finalyear;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


class CustomAdapter extends BaseAdapter {
    public static String URL_NEW_PREDICTION = "http://192.168.137.1/finalyear/updateAttendence.php";
    public static String  json;
    String [] name;
    Context context;
    String [] rgno;
    String sec_id,date;
    private String[] status;

    private static LayoutInflater inflater=null;
    public CustomAdapter(Studentssheet stdsheet, String[] students_name, String[] students_id,String section_id,String dt) {
        // TODO Auto-generated constructor stub
        name=students_name;
        sec_id=section_id;
        date=dt;
        status=new String[name.length];
        context=stdsheet;
        rgno=students_id;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView name;
        TextView rgno;
        Button present;
        Button absent;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();

        view = inflater.inflate(R.layout.customadapter, null);

        holder.name=(TextView) view.findViewById(R.id.name);
        holder.rgno=(TextView) view.findViewById(R.id.rgno);

        holder.name.setText(name[position]);
        holder.rgno.setText(rgno[position]);

        holder.present = (Button)view.findViewById(R.id.btnpresent);
        holder.absent = (Button)view.findViewById(R.id.btnabsent);

        final Button p_btn = holder.present;
        final Button a_btn = holder.absent;

        holder.present.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                status[position] =String.valueOf("0");
                Log.e(String.valueOf(position),status[position]);
               p_btn.setText("P");
               a_btn.setText("");
            }
        });
        holder.absent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                status[position] = String.valueOf("1");
                Log.e(String.valueOf(position), status[position]);
                a_btn.setText("A");
                p_btn.setText("");
            }
        });
        return view;
    }

    protected void myresult(String lec_name){
        int i=0;
        for ( i=0;i<status.length;i++) {
            if (status[i]==null) {
                status[i]="1";
            }
            }
        try {
            for (i=0;i<status.length;i++) {
                new UpdateAttendence().execute(lec_name,sec_id, rgno[i].toString(), status[i].toString(),date);
                Log.e("loop", "End");
            }
        } catch (Exception e) {
            Log.e("err", e.toString());
        }
    }



    public static class UpdateAttendence extends AsyncTask<String, Void, Void> {
    //  public ProgressDialog pdia = new ProgressDialog(CustomAdapter.this);
        Boolean validid=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             //pdia.setMessage("Please wait...");
             //pdia.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub

            String lec_name=arg[0];
            String sec_id = arg[1];
            String std_id= arg[2];
            String attd_id = arg[3];
            String dt=arg[4];
            // Preparing post params
            Log.e("lec_name",lec_name);
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("lec_name", lec_name));
            params.add(new BasicNameValuePair("sec_id", sec_id));
            params.add(new BasicNameValuePair("std_id", std_id));
            params.add(new BasicNameValuePair("attd_id", attd_id));
            params.add(new BasicNameValuePair("dt", dt));


            Servicehandler serviceClient = new Servicehandler();

            json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
                    Servicehandler.GET, params);


            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    Log.d("Update : ", "> " +jsonObj.toString());

                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        Log.e("Update successfully ",
                                "> " + jsonObj.getString("message"));
                    }
                    else {
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

            return null;

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

